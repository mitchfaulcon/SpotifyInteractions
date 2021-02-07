package CLI;

import org.apache.commons.cli.*;

public class CommandLineReader {

    public CommandLine parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();

        buildOptions();
        return parser.parse(buildOptions(), args);
    }

    public Options buildOptions() {
        OptionsWithArgName options = new OptionsWithArgName();

        options.addOption("help", "display command help");

        options.addRequiredOption("u", "username", true, "username", "Spotify username (required)");
        options.addRequiredOption("p", "password", true, "password", "Spotify password (required)");

        options.addOption("s", "shuffle", true, "Playlist Name", "Shuffle input playlist (User credentials must have edit permission)");

        return options;
    }

}

