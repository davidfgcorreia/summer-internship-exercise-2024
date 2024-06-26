package com.premiumminds.internship.teknonymy;

import com.premiumminds.internship.teknonymy.TeknonymyService;
import com.premiumminds.internship.teknonymy.Person;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class TeknonymyServiceTest {

  /**
   * The corresponding implementations to test.
   *
   * If you want, you can make others :)
   *
   */
  public TeknonymyServiceTest() {
  };

  @Test
  public void PersonNoChildrenTest() {
    Person person = new Person("John",'M',null, LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "";
    assertEquals(result, expected);
  }

  @Test
  public void PersonOneChildTest() {
    Person person = new Person("John",'M',
    new Person[]{ new Person("Holy",'F', null, LocalDateTime.of(1046, 1, 1, 0, 0)) },
    LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "father of Holy";
    assertEquals(result, expected);
  }

  @Test
  public void PersonMultiChildTest() {
    Person F = new Person("F", 'M', new Person[]{}, LocalDateTime.of(2010, 1, 1, 0, 0));
    Person G = new Person("G", 'M', new Person[]{}, LocalDateTime.of(2012, 1, 1, 0, 0));
    Person H = new Person("H", 'M', new Person[]{}, LocalDateTime.of(2014, 1, 1, 0, 0));
    Person E = new Person("E", 'M', new Person[]{F, G, H}, LocalDateTime.of(2000, 1, 1, 0, 0));
    Person B = new Person("B", 'M', new Person[]{E}, LocalDateTime.of(1980, 1, 1, 0, 0));
    Person C = new Person("C", 'F', new Person[]{}, LocalDateTime.of(1982, 1, 1, 0, 0));
    Person D = new Person("D", 'F', new Person[]{F, G, H}, LocalDateTime.of(1984, 1, 1, 0, 0));
    Person A = new Person("A", 'M', new Person[]{B, C, D}, LocalDateTime.of(1960, 1, 1, 0, 0));

    TeknonymyService service = new TeknonymyService();

    assertEquals("great-grandfather of F", service.getTeknonymy(A));
    assertEquals("grandfather of F", service.getTeknonymy(B));
    assertEquals("", service.getTeknonymy(C));
    assertEquals("mother of F", service.getTeknonymy(D));
    assertEquals("father of F", service.getTeknonymy(E));
    assertEquals("", service.getTeknonymy(F));
    assertEquals("", service.getTeknonymy(G));
    assertEquals("", service.getTeknonymy(H));
  }

  @Test
  public void PersonMultipleChildrenTest() {
    Person person = new Person(
        "Marta",
        'F',
        new Person[]{
            new Person("Mary", 'F', null, LocalDateTime.of(1020, 1, 1, 0, 0)),
            new Person("Paul", 'M', null, LocalDateTime.of(1025, 1, 1, 0, 0)),
            new Person("Alice", 'F', null, LocalDateTime.of(1030, 1, 1, 0, 0))
        },
        LocalDateTime.of(1000, 1, 1, 0, 0)
    );
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "mother of Mary";
    assertEquals(result, expected);
  }

  @Test
  public void testOneChildOneGrandchildOneGreatGrandchild() {
      Person greatGrandchild = new Person("Great Grandchild", 'M', null, LocalDateTime.of(2040, 1, 1, 0, 0));
      Person grandchild = new Person("Grandchild", 'M', new Person[]{ greatGrandchild }, LocalDateTime.of(2030, 1, 1, 0, 0));
      Person child = new Person("Alice", 'F', new Person[]{ grandchild }, LocalDateTime.of(2010, 1, 1, 0, 0));
      Person person = new Person("John", 'M', new Person[]{ child }, LocalDateTime.of(1980, 1, 1, 0, 0));
      String result = new TeknonymyService().getTeknonymy(person);
      assertEquals("great-grandfather of Great Grandchild", result);
  }


  @Test
  public void test6GenerationsTree() {
      // Geração 1
      Person person10 = new Person("John10", 'M', null, LocalDateTime.of(1000, 1, 1, 0, 0));
      // Geração 2
      Person person9 = new Person("John9", 'M', new Person[]{person10}, LocalDateTime.of(1020, 1, 1, 0, 0));
      Person person8 = new Person("Jane8", 'F', new Person[]{}, LocalDateTime.of(1025, 1, 1, 0, 0));
      // Geração 3
      Person person7 = new Person("John7", 'M', new Person[]{person9}, LocalDateTime.of(1040, 1, 1, 0, 0));
      Person person6 = new Person("Jane6", 'F', new Person[]{person8}, LocalDateTime.of(1045, 1, 1, 0, 0));
      // Geração 4
      Person person5 = new Person("John5", 'M', new Person[]{person7, person6}, LocalDateTime.of(1060, 1, 1, 0, 0));
      // Geração 5
      Person person4 = new Person("Jane4", 'F', new Person[]{}, LocalDateTime.of(1080, 1, 1, 0, 0));
      Person person3 = new Person("John3", 'M', new Person[]{person5, person4}, LocalDateTime.of(1085, 1, 1, 0, 0));
      // Geração 6
      Person person2 = new Person("Jane2", 'F', new Person[]{}, LocalDateTime.of(1100, 1, 1, 0, 0));
      Person person1 = new Person("John1", 'M', new Person[]{person3, person2}, LocalDateTime.of(1110, 1, 1, 0, 0));
      TeknonymyService service = new TeknonymyService();
      assertEquals("great-great-great-grandfather of John10", service.getTeknonymy(person1));
      assertEquals("great-great-grandfather of John10", service.getTeknonymy(person3));
      assertEquals("great-grandfather of John10", service.getTeknonymy(person5));
      assertEquals("", service.getTeknonymy(person8));
      assertEquals("mother of Jane8", service.getTeknonymy(person6));
      assertEquals("father of John10", service.getTeknonymy(person9));
  }

  @Test
  public void testTenGenerationsWithSharedDescendants() {
    // Generation 10
    Person child10 = new Person("Child10", 'M', null, LocalDateTime.of(2080, 1, 1, 0, 0));
    
    // Generation 9
    Person child9a = new Person("Child9a", 'F', new Person[]{child10}, LocalDateTime.of(2070, 1, 1, 0, 0));
    Person child9b = new Person("Child9b", 'M', new Person[]{child10}, LocalDateTime.of(2070, 1, 1, 0, 0));
    // Generation 8
    Person child8a = new Person("Child8a", 'M', new Person[]{child9a, child9b}, LocalDateTime.of(2060, 1, 1, 0, 0));
    Person child8b = new Person("Child8b", 'F', new Person[]{child9a, child9b}, LocalDateTime.of(2060, 1, 1, 0, 0));
    // Generation 7
    Person child7 = new Person("Child7", 'M', new Person[]{child8a, child8b}, LocalDateTime.of(2050, 1, 1, 0, 0));
    // Generation 6
    Person child6 = new Person("Child6", 'F', new Person[]{child7}, LocalDateTime.of(2040, 1, 1, 0, 0));
    // Generation 5
    Person child5 = new Person("Child5", 'M', new Person[]{child6}, LocalDateTime.of(2030, 1, 1, 0, 0));
    // Generation 4
    Person child4 = new Person("Child4", 'F', new Person[]{child5}, LocalDateTime.of(2020, 1, 1, 0, 0));
    // Generation 3
    Person child3 = new Person("Child3", 'M', new Person[]{child4}, LocalDateTime.of(2010, 1, 1, 0, 0));
    // Generation 2
    Person child2 = new Person("Child2", 'F', new Person[]{child3}, LocalDateTime.of(2000, 1, 1, 0, 0));
    // Generation 1 (root)
    Person person = new Person("Root", 'M', new Person[]{child2}, LocalDateTime.of(1990, 1, 1, 0, 0));

    TeknonymyService service = new TeknonymyService();
    String result = service.getTeknonymy(person);
    assertEquals("great-great-great-great-great-great-great-grandfather of Child10", result);
  }


}