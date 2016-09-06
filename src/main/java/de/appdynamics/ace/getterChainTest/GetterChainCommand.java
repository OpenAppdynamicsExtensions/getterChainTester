package de.appdynamics.ace.getterChainTest;

import com.appdynamics.ace.util.cli.api.api.AbstractCommand;
import com.appdynamics.ace.util.cli.api.api.CommandException;
import com.appdynamics.ace.util.cli.api.api.OptionWrapper;
import com.singularity.ee.agent.util.reflect.ReflectionException;
import com.singularity.ee.agent.util.reflect.ReflectionUtility;
import org.apache.commons.cli.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan.marx on 18.06.15.
 */
public class GetterChainCommand extends AbstractCommand {
    @Override
    protected List<Option> getCLIOptionsImpl() {
        ArrayList<Option> options = new ArrayList<Option>();

        Option o = null;
        options.add(o = new Option("input",true,"Input String for Getter Chain"));
        o.setRequired(false);
        options.add(o = new Option("inputFile",true,"Input File for Getter Chain (apply to each line)"));
        o.setRequired(false);

        options.add(o = new Option("getter",true,"Getter Chain to be executed"));
        o.setRequired(true);


        return options;
    }

    @Override
    protected int executeImpl(OptionWrapper optionWrapper) throws CommandException {
        if(optionWrapper.hasOption("input")) {
            String[] inputs = optionWrapper.getOptionValues("input");

            for (String s:inputs) {
                String [] getters =  optionWrapper.getOptionValues("getter");
                parseGetterChain(s,getters);
            }

        }

        if (optionWrapper.hasOption("inputFile")) {
            String [] inputFiles = optionWrapper.getOptionValues("inputFile");
            for (String filename:inputFiles){
                String [] getters =  optionWrapper.getOptionValues("getter");
                parseGetterFile(filename,getters);
            }
        }
        return 0;
    }

    private void parseGetterFile(String filename, String[] getters) {
        File f = new File(filename);

        if (!f.exists() || !f.isFile() || !f.canRead()) {
            System.out.println("Error while opening File :"+filename);
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line = "";
            while ( (line = reader.readLine() )!=null) {
                parseGetterChain(line,getters);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void parseGetterChain(String s, String[] getters) {
        for (String g:getters) {
            parseGetterChain(s,g);
        }
    }

    private void parseGetterChain(String s, String g) {
        String name = s;
        if (s.length()>15) {
            name = s.substring(0,15)+" ...";
        }
        System.out.println("Parse  : "+name);
        System.out.println("Getter : "+g);

        try {
            String result = ""+ReflectionUtility.invokeSimpleGetterRecursiveWithString(s,g);
            System.out.println("--> "+result);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Test the getter chain";
    }
}
