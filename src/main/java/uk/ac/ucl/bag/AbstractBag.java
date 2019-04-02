package uk.ac.ucl.bag;

/**
 * This class implements methods common to all concrete bag implementations
 * but does not represent a complete bag implementation.<br />
 *
 * New bag objects are created using a BagFactory, which can be configured in the application
 * setup to select which bag implementation is to be used.
 */

public abstract class AbstractBag<T extends Comparable> implements Bag<T>
{
  public Bag<T> createMergedAllOccurrences(Bag<T> b) throws BagException {
    Bag<T> result = BagFactory.getInstance().getBag();
    for (T value : this)
    {
      result.addWithOccurrences(value, this.countOf(value));
    }
    for (T value : b)
    {
      result.addWithOccurrences(value, b.countOf(value));
    }
    return result;
  }

  public Bag<T> createMergedAllUnique(Bag<T> b) throws BagException {
    Bag<T> result = BagFactory.getInstance().getBag();
    for (T value : this)
    {
      if (!result.contains(value)) result.add(value);
    }
    for (T value : b)
    {
      if (!result.contains(value)) result.add(value);
    }
    return result;
  }

  @Override
  public String toString(){
      boolean first = true;
      String str ="";
      str += "[";
      for (T value : this){
          if (!first){str += ", ";}
          first = false;

          T key = value;
          int occurrence = this.countOf(value);
          str += key;
          str += ":";
          str += occurrence;
      }

      str += "]";

      return str;
  }


  //It makes sure that the occurrence count of all values stored in a Bag is set to one
  public void removeAllCopies(){
      for (T value : this){
          int occurrences = this.countOf(value);
          while (occurrences > 1){
              this.remove(value);
              --occurrences;
          }
      }

  }


    // returns a new Bag containing all values and occurrences that occur in the 'this' bag but not the argument bag
  public Bag<T> subtract(Bag<T> bag){

      for (T value : bag){
          if (bag.contains(value) && this.contains(value)){
              int occurrences = bag.countOf(value);
              for (int i = 0; i < occurrences; i++){
                  this.remove(value);
              }
          }
      }
      return this;
  }

}
