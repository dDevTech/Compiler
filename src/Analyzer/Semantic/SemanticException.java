package Analyzer.Semantic;

public class SemanticException extends Exception{
    private final int errorCode;
    private final String message;

    public SemanticException(int errorCode, String message){

        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
