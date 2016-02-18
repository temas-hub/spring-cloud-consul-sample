package org.springframework.cloud.consul.sample;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author Artem Zhdanov <azhdanov@griddynamics.com>
 * @since 17.02.2016
 */
public class ReloadSignalHandler implements SignalHandler{



    private RefreshScope refreshScope;

    private String signalName;

    public ReloadSignalHandler(RefreshScope refreshScope, String signalName) {
        this.refreshScope = refreshScope;
        this.signalName = signalName;
        Signal diagSignal = new Signal(signalName);
        this.oldHandler = Signal.handle(diagSignal, this);
    }



    private SignalHandler oldHandler;
    // Static method to install the signal handler
    public static ReloadSignalHandler install(String signalName) {
        Signal diagSignal = new Signal(signalName);
        ReloadSignalHandler diagHandler = new ReloadSignalHandler(null,null);
        diagHandler.oldHandler = Signal.handle(diagSignal, diagHandler);
        return diagHandler;
    }


        @Override
    public void handle(final Signal sig) {
        try {
            System.out.println("Worked!!!");
            //refreshScope.refreshAll();
//            final RefreshScope refreshScope = applicationContext.getBean(RefreshScope.class);
//            refreshScope.refreshAll();
//            if ( oldHandler != SIG_DFL && oldHandler != SIG_IGN ) {
//                oldHandler.handle(sig);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
