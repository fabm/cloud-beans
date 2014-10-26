package pt.gapiap.servlets;

import com.google.api.server.spi.ObjectMapperUtil;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import pt.gapiap.cloud.endpoints.EndpointReturn;
import pt.gapiap.cloud.endpoints.authorization.UserWithRoles;
import pt.gapiap.cloud.endpoints.errors.FailureManager;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContent;
import pt.gapiap.cloudEndpoints.services.annotations.InstanceType;
import pt.gapiap.cloudEndpoints.services.annotations.PhotoUploadClass;
import pt.gapiap.cloudEndpoints.services.annotations.PhotoUploadMethod;
import pt.gapiap.cloudEndpoints.services.annotations.PhotoUploadedKey;
import pt.gapiap.guice.CurrentEmailProvider;
import pt.gapiap.guice.UserWithRolesProvider;
import pt.gapiap.servlets.language.UploadErrorsContent;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class Upload extends HttpServlet {
  private static Map<String, UploadClassMethod> uploadMethodMap;
  @Inject
  private FailureManager failureManager;
  @Inject
  private CurrentEmailProvider currentEmailProvider;
  @Inject
  private UserWithRolesProvider<?> userWithRolesProvider;

  static {
    uploadMethodMap = new TreeMap<>();
  }

  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  private static ObjectMapper addObjectMapper(HttpServletResponse res) {
    res.setContentType("application/json");
    return ObjectMapperUtil.createStandardObjectMapper();
  }

  private static Map<String, Object> errorMap(String message, int code) {
    Map<String, Object> mapError = new HashMap<>();
    mapError.put("failCode", code);
    mapError.put("message", message);
    return mapError;
  }

  public void registerUploadClass(Class<?> uploadClass) {
    PhotoUploadClass photoUploadClassAn = uploadClass.getAnnotation(PhotoUploadClass.class);
    if (uploadClass == null) {
      throw new RuntimeException("It's not possible to register a null class");
    }
    if (photoUploadClassAn == null) {
      String errorMessage = "Requires the annotation @PhotoUploadClass in '%s' class";
      errorMessage = String.format(errorMessage, uploadClass.getSimpleName());
      throw new RuntimeException(errorMessage);
    }

    UploadClassMethod uploadClassMethod = new UploadClassMethod();
    uploadClassMethod.clazz = uploadClass;
    uploadClassMethod.type = photoUploadClassAn.type();

    boolean hasMethods = false;
    for (Method method : uploadClass.getDeclaredMethods()) {
      PhotoUploadMethod photoUploadMethodAn = method.getAnnotation(PhotoUploadMethod.class);
      if (photoUploadMethodAn != null) {
        String key = photoUploadMethodAn.key();
        if (key == null) {
          String errorMessage = "key of method '%s' must be a not null String";
          errorMessage = String.format(errorMessage, method.getName());
          throw new RuntimeException(errorMessage);
        }
        if (key.isEmpty()) {
          String errorMessage = "key of method '%s' must be a not empty String";
          errorMessage = String.format(errorMessage, method.getName());
          throw new RuntimeException(errorMessage);
        }
        if (uploadMethodMap.containsKey(key)) {
        }
        uploadMethodMap.put(key, uploadClassMethod);
        uploadClassMethod.methodsMap.put(key, method);
        hasMethods = true;
      }
    }
    if (!hasMethods) {
      String errorMessage = "the class '%s' don't has methods annotated with @PhotoUploadMethod";
      errorMessage = String.format(errorMessage, uploadClassMethod.clazz.getSimpleName());
      throw new RuntimeException(errorMessage);
    }

  }


  protected abstract UnauthorizedException errorAuthorizationOauth();

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {


    List<String> params = UrlParameters.getParameters(req);
    Map<String, Object> map = new HashMap<String, Object>();
    if (!params.isEmpty()) {
      throw failureManager.createError(UploadErrorsContent.NO_ACTION_PARAMETER);
    }
    String token = params.get(0);
    try {
      currentEmailProvider.loadFromOAuth2();
      map.put("email", currentEmailProvider.get());
    } catch (UnauthorizedException e) {
      throw failureManager.createError(GlobalContent.NOT_AUTHORIZED);
    }
    addObjectMapper(res);
  }


  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    ObjectMapper objectMapper = addObjectMapper(res);


    EndpointReturn endpointReturn = null;
    String errorMessageReturnMap = "The method '%s' must return a Map<String,Object>";
    try {
      String action = req.getParameter("action");
      if (action == null || action.isEmpty()) {
        throw failureManager.createError(UploadErrorsContent.NO_ACTION_PARAMETER, req.getLocale().getLanguage());
      }

      UploadClassMethod<? extends UserWithRoles> uploadClassMethod = uploadMethodMap.get(action);
      if (uploadClassMethod == null) {
        //throw new CEError(GlobalUploadError.NO_UPLOAD_ACTION_REGISTERED, action);
      }
      Method method = uploadClassMethod.methodsMap.get(action);

      errorMessageReturnMap = String.format(errorMessageReturnMap, method.getName());
      if (method.getReturnType() != Map.class) {
        throw new RuntimeException(errorMessageReturnMap);
      }

      Annotation[][] arr = method.getParameterAnnotations();
      Object[] parameterValues = new Object[arr.length];

      String errorMessage = "The method '%s' has parameters without @Named annotation";
      errorMessage = String.format(errorMessage, method.getName());

      boolean noNamedAnnotation = true;
      boolean noPhotoUploadedKey = true;
      for (int i = 0; i < arr.length; i++) {
        if (arr[i].length == 0) {
          throw new RuntimeException(errorMessage);
        }
        for (Annotation annotation : arr[i]) {
          if (annotation.annotationType() == Named.class) {
            Named named = (Named) annotation;
            parameterValues[i] = req.getParameter(named.value());
            if (method.getParameterTypes()[i] == Long.class) {
              parameterValues[i] = Long.parseLong(parameterValues[i].toString());
            } else if (method.getParameterTypes()[i] == Integer.class) {
              parameterValues[i] = Integer.parseInt(parameterValues[i].toString());
            }
            noNamedAnnotation = false;
            break;
          }
          if (annotation.annotationType() == PhotoUploadedKey.class) {
            if (method.getParameterTypes()[i] != String.class) {
              throw new RuntimeException("the parameter with @PhotoUploadedKey must be a String");
            }
            noPhotoUploadedKey = false;
            Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
            List<BlobKey> mapFile = blobs.get("file");
            parameterValues[i] = mapFile.get(0).getKeyString();
          }
        }
      }
      if (noPhotoUploadedKey) {
        throw new RuntimeException("There is no parameter annotated with @PhotoUploadedKey");
      }

      if (noNamedAnnotation) {
        throw new RuntimeException(errorMessage);
      }
      currentEmailProvider.loadFromOAuth2();
      String email = currentEmailProvider.get();
      userWithRolesProvider.setEmail(email);
      endpointReturn = (EndpointReturn) method.invoke(userWithRolesProvider.get(), parameterValues);
    } catch (IllegalAccessException e) {
      endpointReturn = new EndpointReturn(failureManager.createFailure(GlobalContent.UNEXPECTED));
      e.printStackTrace();
    } catch (UnauthorizedException e) {
      endpointReturn = new EndpointReturn(failureManager.createFailure(GlobalContent.NOT_AUTHORIZED));
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      endpointReturn = new EndpointReturn(failureManager.createFailure(GlobalContent.UNEXPECTED));
      e.printStackTrace();
    }
    objectMapper.writeValue(res.getWriter(), endpointReturn);
  }


  private static class UploadClassMethod<T extends UserWithRoles<?>> {
    public Class<T> clazz;
    public InstanceType type;
    public Map<String, Method> methodsMap = new HashMap<String, Method>();
    private Object instance;

    public UserWithRoles<?> getInstace() throws IllegalAccessException, InstantiationException {
      if (this.type == InstanceType.NEW_INSTANCE) {
        return clazz.newInstance();
      }
      if (instance == null) {
        instance = clazz.newInstance();
      }
      return (UserWithRoles<?>) instance;
    }
  }
}
