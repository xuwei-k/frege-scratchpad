package test ;

import frege.prelude.PreludeBase ;
import frege.prelude.PreludeList ;
import java.util.NoSuchElementException ;
import java.lang.UnsupportedOperationException ;
import java.util.Iterator ;

class JFregeIterator<T> implements Iterator<T> {
  PreludeBase.TList fregeListCursor ;

  // private constructor
  private JFregeIterator(PreludeBase.TList fregeListCursor) {
    this.fregeListCursor = fregeListCursor ; 
  }

  // instances will be created in Frege
  public static <T> JFregeIterator<T> getInstance(PreludeBase.TList fregeListCursor) {
    return new JFregeIterator<T>( fregeListCursor) ;
  }

  public boolean hasNext() {
    // return ! FregeIteratorUtil.listIsEmpty( fregeListCursor) ;
    return ! PreludeList.IListLike__lbrack_rbrack._null( fregeListCursor) ;

  }

  public T next() throws NoSuchElementException {
    if (!hasNext()) throw new NoSuchElementException() ;
    
    // T result = (T) FregeIteratorUtil.listHead( fregeListCursor) ;
    T result = (T) PreludeList.IListLike__lbrack_rbrack.head( fregeListCursor) ;

    // fregeListCursor = FregeIteratorUtil.listTail( fregeListCursor) ;
    fregeListCursor = PreludeList.IListLike__lbrack_rbrack.tail( fregeListCursor) ;
    return result ;
  }

  public void remove() throws UnsupportedOperationException {
    throw new UnsupportedOperationException() ;
  }
}