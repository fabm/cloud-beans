package pt.gapiap.session;

public interface SessionContainerCreator<T extends SessionContainer<?>> {
    T newSessionContainer();
}
