package de.appdynamics.ace.getterChainTest;

import com.appdynamics.ace.util.cli.api.api.Command;
import com.appdynamics.ace.util.cli.api.api.CommandlineExecution;

/**
 * Created by stefan.marx on 18.06.15.
 */
public class Main {

    public static void main(String[] args) {


        CommandlineExecution cle = new CommandlineExecution("getterChain");
        cle.setHelpVerboseEnabled(false);

        Command c;

        cle.addCommand(c = new GetterChainCommand());

        System.exit(cle.execute(args));

    }
}
