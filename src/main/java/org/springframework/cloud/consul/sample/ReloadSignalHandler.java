package org.springframework.cloud.consul.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author Artem Zhdanov <azhdanov@griddynamics.com>
 * @since 17.02.2016
 */
public class ReloadSignalHandler implements SignalHandler {

    @Autowired
    private RefreshScope refreshScope;

    private SignalHandler oldHandler;
    // Static method to install the signal handler
    public static ReloadSignalHandler install(String signalName) {
        Signal diagSignal = new Signal(signalName);
        ReloadSignalHandler diagHandler = new ReloadSignalHandler();
        diagHandler.oldHandler = Signal.handle(diagSignal, diagHandler);
        return diagHandler;
    }


        @Override
    public void handle(final Signal sig) {
        try {
            System.out.println("Worked!!!");
            refreshScope.refreshAll();
            if ( oldHandler != SIG_DFL && oldHandler != SIG_IGN ) {
                oldHandler.handle(sig);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
