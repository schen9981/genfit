package edu.brown.cs.rankers;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Item;
import com.genfit.proxy.dummies.ItemProxyDummy;
import com.genfit.rankers.AttributeRankerFactory;
import com.genfit.rankers.ColorAttrRanker;
import com.genfit.rankers.FormalityAttrRanker;
import com.genfit.rankers.PatternAttrRanker;
import com.genfit.rankers.SeasonAttrRanker;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class RankerTest {
  private ColorAttrRanker car;
  private SeasonAttrRanker sar;
  private PatternAttrRanker par;
  private FormalityAttrRanker far;
  private AttributeRankerFactory arf;

  private static void printList(List l) {
    Iterator i = l.iterator();
    while (i.hasNext()) {
      System.out.print(i.next());
      System.out.print(", ");
    }
    System.out.println("");
  }

  @Before
  public void setUp() {
    this.car = new ColorAttrRanker();
    this.sar = new SeasonAttrRanker();
    this.par = new PatternAttrRanker();
    this.far = new FormalityAttrRanker();
    this.arf = new AttributeRankerFactory();
  }

  @Test
  public void testBasic() {
    assertEquals(this.arf.rankAttributes(this.sar).size(), 0);
    assertEquals(this.arf.rankAttributes(this.par).size(), 0);
    assertEquals(this.arf.rankAttributes(this.far).size(), 0);

//    Item i1 = new Item(1, "dummy1",
//            new SeasonAttribute(SeasonEnum.FALL),
//            new FormalityAttribute(FormalityEnum.BUSINESS_CASUAL),
//            new PatternAttribute(PatternEnum.SOLID),
//            new ColorAttribute(new Color(Color.convertToHexVal(62, 131, 242))),
//            new TypeAttribute(TypeEnum.BOTTOM),
//            "https://s3.amazonaws.com/cs32-term-project-s3-bucket/pants.png");

//    this.arf.addItem(new ItemProxyDummy(i1));

    //printList(this.arf.rankAttributes(this.sar));
    //printList(this.arf.rankAttributes(this.par));
    //printList(this.arf.rankAttributes(this.far));

//    Item i2 = new Item(2, "dummy2",
//            new SeasonAttribute(SeasonEnum.SPRING),
//            new FormalityAttribute(FormalityEnum.ULTRA_CASUAL),
//            new PatternAttribute(PatternEnum.STRIPED),
//            new ColorAttribute(new Color(4567)),
//            new TypeAttribute(TypeEnum.TOP),
//            "https://s3.amazonaws.com/cs32-term-project-s3-bucket/shirt.png");
//
//    this.arf.addItem(new ItemProxyDummy(i2));

    //printList(this.arf.rankAttributes(this.car));
    //printList(this.arf.rankAttributes(this.sar));
    //printList(this.arf.rankAttributes(this.par));
    //printList(this.arf.rankAttributes(this.far));
  }
}
