package eredua.bean;

import java.util.logging.Level;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import eredua.BetsLogger;

public class FacadeBean {
    
    private static FacadeBean singleton = new FacadeBean();
    private static BLFacade facadeInterface;
    
    private FacadeBean() {
        try {
            facadeInterface = new BLFacadeImplementation();
        }
        catch (Exception e) {
            BetsLogger.log(Level.SEVERE, String.format("FacadeBean: negozioaren logika sortzean errorea: %s",
                    e.getMessage()));
        }
    }
    
    public static BLFacade getBusinessLogic() {
        return facadeInterface;
    }
}
