package pt.json.runtime.cloud.endpoints;

import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.authorization.ACLCreator;
import pt.gapiap.cloud.endpoints.authorization.ACLInvokerCreator;
import pt.json.runtime.cloud.services.ServiceTestImpl;

public class TestCloudEndPoint {
  @Inject
  private ACLCreator aclCreator;

  @Inject
  private void addService(ACLInvokerCreator aclInvokerCreator){
    aclInvokerCreator.addServiceInstance(new ServiceTestImpl());
  }
}
