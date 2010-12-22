package COM.sootNsmoke.jvm;

public class ClassFileException extends Exception {
    public ClassFileException(String message) {
	super(message);
    }
}

class BadClassFileException extends ClassFileException {
    public BadClassFileException(String message) {
	super(message);
    }
}

class BadConstantTagException extends ClassFileException {
    public BadConstantTagException(String message) {
	super(message);
    }
}

class BadAttributeException extends ClassFileException {
    public BadAttributeException(String message) {
	super(message);
    }
}

class BadConstantException extends ClassFileException {
    public BadConstantException(String message) {
	super(message);
    }
}

