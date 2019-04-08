package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ParserMode;

/**
 * Returns the total number of sources in the database.
 */
public class CountCommand extends Command {

    public static final String COMMAND_WORD = "count";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Counts the number of source(s) in the Infinity Machine.";

    public static final String MESSAGE_SUCCESS = "Total number of Source(s): %1$s";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        if (model.getParserMode() == ParserMode.RECYCLE_BIN) {
            model.switchToDeletedSources(); // sets deleted source data to list
        } else {
            model.switchToSources(); // sets source manager data to list
        }
        int count = model.getFilteredSourceList().size();
        return new CommandResult(String.format(MESSAGE_SUCCESS, count));
    }

}
