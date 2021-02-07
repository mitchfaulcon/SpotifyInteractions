package CLI;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsWithArgName extends Options {
    public Options addOption(String opt, String longOpt, boolean hasArg, String argName, String description) {
        Option option = new Option(opt, longOpt, hasArg, description);
        option.setArgName(argName);
        this.addOption(option);
        return this;
    }

    public Options addRequiredOption(String opt, String longOpt, boolean hasArg, String argName, String description) {
        Option option = new Option(opt, longOpt, hasArg, description);
        option.setRequired(true);
        option.setArgName(argName);
        this.addOption(option);
        return this;
    }
}
