package seedu.address.model.source;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.source.exceptions.DuplicateSourceException;
import seedu.address.model.source.exceptions.SourceNotFoundException;

/**
 * A list of deleted sources that enforces uniqueness between its elements and does not allow nulls.
 * A source is considered unique by comparing using {@code Source#isSameSource(Source)}. As such, adding and updating of
 * sources uses Source#isSameSource(Source) for equality so as to ensure that the source being added or updated is
 * unique in terms of identity in the UniqueDeletedSourceList.
 * However, the removal of a source uses Source#equals(Object) so
 * as to ensure that the source with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Source#isSameSource(Source)
 */
public class UniqueDeletedSourceList implements Iterable<Source> {

    private final ObservableList<Source> internalDeletedList = FXCollections.observableArrayList();
    private final ObservableList<Source> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalDeletedList);

    /**
     * Returns true if the list contains an equivalent source as the given argument.
     */
    public boolean contains(Source toCheck) {
        requireNonNull(toCheck);
        return internalDeletedList.stream().anyMatch(toCheck::isSameSource);
    }

    /**
     * Adds a source to the list.
     * The source must not already exist in the list.
     */
    public void add(Source toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateSourceException();
        }
        internalDeletedList.add(toAdd);
    }

    /**
     * Adds a source to the list at a specified index.
     * The source must not already exist in the list.
     */
    public void addAtIndex(Source toAdd, int index) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateSourceException();
        }
        internalDeletedList.add(index, toAdd);
    }

    /**
     * Replaces the source {@code target} in the list with {@code editedSource}.
     * {@code target} must exist in the list.
     * The source identity of {@code editedSource} must not be the same as another existing source in the list.
     */
    public void setSource(Source target, Source editedSource) {
        requireAllNonNull(target, editedSource);

        int index = internalDeletedList.indexOf(target);
        if (index == -1) {
            throw new SourceNotFoundException();
        }

        if (!target.isSameSource(editedSource) && contains(editedSource)) {
            throw new DuplicateSourceException();
        }

        internalDeletedList.set(index, editedSource);
    }

    /**
     * Removes the equivalent source from the list.
     * The source must exist in the list.
     */
    public void remove(Source toRemove) {
        requireNonNull(toRemove);
        if (!internalDeletedList.remove(toRemove)) {
            throw new SourceNotFoundException();
        }
    }

    public void setSources(UniqueDeletedSourceList replacement) {
        requireNonNull(replacement);
        internalDeletedList.setAll(replacement.internalDeletedList);
    }

    /**
     * Replaces the contents of this list with {@code sources}.
     * {@code sources} must not contain duplicate sources.
     */
    public void setSources(List<Source> sources) {
        requireAllNonNull(sources);
        if (!sourcesAreUnique(sources)) {
            throw new DuplicateSourceException();
        }

        internalDeletedList.setAll(sources);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Source> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Source> iterator() {
        return internalDeletedList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueDeletedSourceList // instanceof handles nulls
                && internalDeletedList.equals(((UniqueDeletedSourceList) other).internalDeletedList));
    }

    @Override
    public int hashCode() {
        return internalDeletedList.hashCode();
    }

    /**
     * Returns true if {@code sources} contains only unique sources.
     */
    private boolean sourcesAreUnique(List<Source> sources) {
        for (int i = 0; i < sources.size() - 1; i++) {
            for (int j = i + 1; j < sources.size(); j++) {
                if (sources.get(i).isSameSource(sources.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
