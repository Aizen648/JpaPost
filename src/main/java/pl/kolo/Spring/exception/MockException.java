package pl.kolo.Spring.exception;

public class MockException extends RuntimeException {
    public MockException(long number){
        super("error number "+number+" Not implemented yet, and will never be !!!");

    }
}
