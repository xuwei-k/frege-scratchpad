package test ;

// singleton class
public class ModelRoot<T> {

   T modelStructure ;

   static ModelRoot modelRoot = null ;

   // private constructor
   private ModelRoot( T modelStructure) {
       this.modelStructure = modelStructure ;
   }

   public static <T> ModelRoot initSingleton( T modelStructure) { // ModelRoot
     if (modelRoot == null) {
          modelRoot = new ModelRoot( modelStructure) ;
     }
     else {
        modelRoot.setModelStructure( modelStructure) ;
     }
     return modelRoot ;
   }
   public static ModelRoot getInstance() {
     return modelRoot ;
   }

   public T getModelStructure() {
     return modelStructure ;
   }

   public void setModelStructure( T modelStructure) {
     this.modelStructure = modelStructure ; 
   }
}