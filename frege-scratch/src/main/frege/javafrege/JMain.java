package test ;

import test.Model ;
import test.Model.TSample ;
import test.JFregeIterator ;
import frege.rt.Box ;
import frege.rt.Lambda ;
import java.util.NoSuchElementException ;
import frege.prelude.PreludeBase ;
import frege.prelude.PreludeList ;


class JMain {
	static public void main( String [] args) {
            Box.Int unit = Box.Int.mk(0) ;

            Model.modelInit( unit) ;
            Model.modelAdd( Box.<String>mk("London"), Box.Int.mk(9)) ;

            JFregeIterator<TSample> iter = (JFregeIterator<TSample>) ((Box<JFregeIterator>) Model.modelGetListIterator( unit)).j ;

            int cnt = 0 ;
            while (iter.hasNext() && cnt < 10) {

                try {
			TSample obj = (TSample) iter.next() ; 
			String str = TSample.city( obj) ;
			System.out.println( str) ; 
                }
                catch (NoSuchElementException e) {
                        System.out.println( e.getMessage()) ;
                }  
                cnt++ ; 
            }
	}
}