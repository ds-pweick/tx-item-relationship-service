package net.catenax.irs.exceptions;

public class NotFoundException extends Exception {

   public NotFoundException() {
   }

   public NotFoundException(String message) {
      super(message);
   }

   public NotFoundException(Throwable cause) {
      super(cause);
   }

   @Override
   public String toString() {
      return "NotFoundException: "+getMessage();
   }
}
