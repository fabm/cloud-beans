package pt.gapiap.proccess.wrappers;

import com.google.inject.Inject;
import pt.gapiap.proccess.annotations.Embedded;
import pt.gapiap.proccess.annotations.InvalidAnnotation;
import pt.gapiap.proccess.logger.Logger;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;
import java.util.Iterator;

public class ValidationRulesIteratable implements Iterable<ValidationRule>, Iterator<ValidationRule> {
    private @Inject
    Logger logger;
    private boolean isFirstAnnotation = true;
    private boolean markedAsEmbedded = false;
    private Iterator<Validation> validationsIterator;
    private Iterator<? extends AnnotationMirror> annotationsIterator;
    private Validation currentValidation;
    private AnnotationMirror currentAnnotation;
    private Iterable<Validation> validationsIteratable;
    private Iterable<? extends AnnotationMirror> annotationsIterable;

    public ValidationRulesIteratable(VariableElement variableElement, Iterable<Validation> validationsIterable) {

        logger.log("construct:" + variableElement);
        this.validationsIteratable = validationsIterable;
        this.annotationsIterable = variableElement.getAnnotationMirrors();
    }

    private static InvalidAnnotation getInvalidAnnotationEmbeddedError() {
        return new InvalidAnnotation("It's not possible to mark other annotations on fields that have " +
                Embedded.class.getCanonicalName() + " annotation");
    }

    @Override
    public boolean hasNext() {
        if (nextValid()) {
            return true;
        }
        logger.log("no next:" + "\n");
        return false;
    }

    private boolean nextValid() {
        while (nextCombination()) {
            if (markedAsEmbedded) {
                throw getInvalidAnnotationEmbeddedError();
            }
            if (currentValidation.getDeclaredType().toString().equals(currentAnnotation.getAnnotationType().toString())) {
                return true;
            }
            if (currentAnnotation.getAnnotationType().toString().equals("pt.gapiap.proccess.annotations.Embedded")) {
                if (isFirstAnnotation) {
                    throw getInvalidAnnotationEmbeddedError();
                }
                markedAsEmbedded = true;
                return true;
            }
        }
        logger.log("back false:" + "\n");
        return false;
    }

    private boolean nextCombination() {
        logger.log("validationsIterator:" + validationsIterator.hasNext() + "\n");
        logger.log("annotationsIterator:" + annotationsIterator.hasNext() + "\n");
        logger.log("cn:" + currentAnnotation + "\n");
        logger.log("cv:" + currentValidation + "\n");
        if (validationsIterator.hasNext()) {
            currentValidation = validationsIterator.next();
            isFirstAnnotation = false;
            logger.log("cn:" + currentAnnotation + "\n");
            logger.log("cv:" + currentValidation + "\n");
            return true;
        }
        if (annotationsIterator.hasNext()) {
            validationsIterator = validationsIteratable.iterator();
            currentAnnotation = annotationsIterator.next();
            isFirstAnnotation = true;
            logger.log("cn:" + currentAnnotation + "\n");
            logger.log("cv:" + currentValidation + "\n");
            return true;
        }
        logger.log("fim:" + "\n");
        return false;
    }

    @Override
    public ValidationRule next() {
        ValidationRule validationRule = new ValidationRule();
        validationRule.setAnnotationMirror(currentAnnotation);
        return validationRule;
    }

    @Override
    public void remove() {

    }

    @Override
    public Iterator<ValidationRule> iterator() {
        logger.log("init:\n");
        validationsIterator = validationsIteratable.iterator();
        annotationsIterator = annotationsIterable.iterator();
        if (validationsIterator.hasNext()) {
            currentValidation = validationsIterator.next();
        }
        if (annotationsIterator.hasNext()) {
            currentAnnotation = annotationsIterator.next();
        }
        return this;
    }
}
