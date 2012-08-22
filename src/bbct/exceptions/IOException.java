package bbct.exceptions;

/**
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class IOException extends Exception {
    
    /**
     * 
     * @param source
     */
    public IOException(Exception source) {
        super(source);
    }
    
}
