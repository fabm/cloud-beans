package pt.gapiap.cloud.endpoints.errors;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ErrorManager implements Iterable<ErrorIdentifier> {



  private ArrayList<ErrorLocale> errorLocales;

  public int errorsRegister(final List<ErrorLocale> listToRegister) {
    errorLocales = Optional.fromNullable(errorLocales).or(new Supplier<ArrayList<ErrorLocale>>() {
      @Override
      public ArrayList<ErrorLocale> get() {
        return new ArrayList<ErrorLocale>(listToRegister.size());
      }
    });

    int initSize = errorLocales.size();
    errorLocales.addAll(listToRegister);
    return initSize;
  }

  public ErrorIdentifier getError(int index) {
    Optional<ErrorLocale> optErrorLocal = Optional.fromNullable(errorLocales.get(index));

    if (optErrorLocal.isPresent()) {
      return new ErrorIdentifier(index, optErrorLocal.get());
    }

    return null;
  }

  @Override
  public Iterator<ErrorIdentifier> iterator() {
    Iterable<ErrorLocale> errorLocalesIterable = Iterables.filter(errorLocales, new PredicateErrorClient());
    final Iterator<ErrorLocale> errorLocalesIterator = errorLocalesIterable.iterator();

    Iterator<ErrorIdentifier> errorIdentifierIterator = new Iterator<ErrorIdentifier>() {
      private int i = 0;

      @Override
      public boolean hasNext() {
        return errorLocalesIterator.hasNext();
      }

      @Override
      public ErrorIdentifier next() {
        ErrorLocale current = errorLocalesIterator.next();
        return new ErrorIdentifier(i++, current);
      }

      @Override
      public void remove() {

      }
    };
    return errorIdentifierIterator;
  }
}
