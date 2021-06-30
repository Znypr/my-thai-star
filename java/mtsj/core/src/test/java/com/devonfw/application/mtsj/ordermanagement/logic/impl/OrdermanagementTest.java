package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.dishmanagement.common.api.to.DishEto;
import com.devonfw.application.mtsj.dishmanagement.common.api.to.IngredientEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.Dishmanagement;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoBookingException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoInviteException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.OrderAlreadyExistException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.WrongTokenException;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderedDishesSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderedDishesSearchCriteriaTo.Type;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;

/**
 * Test for {@link Ordermanagement}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class OrdermanagementTest extends ApplicationComponentTest {

  private static Instant instant = Instant.now();

  @Inject
  private Ordermanagement orderManagement;

  OrderCto orderCto;

  OrderCto orderCto2;

  @Inject
  private Dishmanagement dishManagement;

  @Inject
  private Bookingmanagement bookingManagement;

  @Inject
  private Usermanagement userManagement;

  /**
   * Creation of needed objects
   */
  @Override
  public void doSetUp() {

    super.doSetUp();

    // extra ingredients
    IngredientEto i1 = new IngredientEto();
    i1.setId(0L);
    IngredientEto i2 = new IngredientEto();
    i2.setId(1L);
    List<IngredientEto> extras = new ArrayList<>();
    extras.add(i1);
    extras.add(i2);

    // Dish
    DishEto dishEto = new DishEto();
    dishEto.setId(5L);

    // OrderLine Eto 1
    OrderLineEto olEto1 = new OrderLineEto();
    olEto1.setAmount(3);
    olEto1.setComment("This is a test order line");
    olEto1.setDishId(dishEto.getId());

    // OrderLine Eto 2
    OrderLineEto olEto2 = new OrderLineEto();
    olEto2.setAmount(1);
    olEto2.setComment("This is another order line");
    olEto2.setDishId(dishEto.getId());

    // order line 1
    OrderLineCto ol1 = new OrderLineCto();
    ol1.setDish(dishEto);
    ol1.setOrderLine(olEto1);
    ol1.setExtras(extras);

    // order line 2
    OrderLineCto ol2 = new OrderLineCto();
    ol2.setDish(dishEto);
    ol1.setOrderLine(olEto2);

    // order
    List<OrderLineCto> lines = new ArrayList<>();
    lines.add(ol1);
    // lines.add(ol2);

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("CB_20170510_123502595Z");
    this.orderCto = new OrderCto();
    this.orderCto.setBooking(bookingEto);
    this.orderCto.setOrderLines(lines);
    this.orderCto.setOrderStatus("open");

    // needs to change setBooking

    // BookingEto bookingEto2 = new BookingEto();
    // bookingEto2.setBookingToken("CB_20170510_123502567Z");
    // BookingCto bookingCto = new BookingCto();
    // bookingCto.setBooking(bookingEto2);
    // BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);

    // this.orderCto2 = new OrderCto();
    // this.orderCto2.setBooking(createdBooking);
    // this.orderCto2.setOrderLines(lines);
    // this.orderCto2.setOrderStatus("open");

  }

  /**
   * Tests if an order is created and values are identical
   */
  @Test
  public void orderAnOrder() {

    BookingEto bookingEto2 = new BookingEto();
    bookingEto2.setName("tester2");
    bookingEto2.setEmail("test2@test.de");
    bookingEto2.setBookingDate(new Timestamp(100L).toInstant());

    BookingCto bookingCto = new BookingCto();
    bookingCto.setBooking(bookingEto2);

    BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
    OrderCto order = new OrderCto();
    order.setBooking(createdBooking);

    List<OrderLineCto> lines = new ArrayList<>();
    order.setOrderLines(lines);

    OrderEto createdOrder = this.orderManagement.saveOrder(order);
    assertThat(createdOrder).isNotNull();

    // assertThat(createdOrder).isNotNull();
    // assertThat(this.orderManagement.findOrder(createdOrder.getId()).getOrderStatus())
    // .isEqualTo(this.orderCto2.getOrderStatus());
    // assertThat(this.orderManagement.findOrder(createdOrder.getId()).getBooking().getBookingToken())
    // .isEqualTo(this.orderCto2.getBooking().getBookingToken());
    // assertThat(this.orderManagement.findOrder(createdOrder.getId()).getOrderLines().size())
    // .isEqualTo(this.orderCto2.getOrderLines().size());

  }

  /**
   * Tests if an order is created and values are identical
   */
  @Test
  public void updateOrderAndGetExtras() {

    OrderEto createdOrder = this.orderManagement.saveOrder(this.orderCto);
    assertThat(createdOrder).isNotNull();
    assertThat(this.orderCto.getOrderLines().get(0).getDish()).isNotNull();

    // extra ingredients
    IngredientEto i1 = new IngredientEto();
    i1.setId(0L);
    IngredientEto i2 = new IngredientEto();
    i2.setId(1L);
    List<IngredientEto> extras = new ArrayList<>();
    extras.add(i1);
    extras.add(i2);

    assertThat(this.orderCto.getOrderLines().get(0).getExtras().toString()).isEqualTo(extras.toString());

  }

  /**
   * Tests if an order is created and values are identical
   */
  @Test
  public void updateInvitedGuests() {

    BookingEto bookingEto2 = new BookingEto();
    bookingEto2.setName("tester3");
    bookingEto2.setEmail("test3@test.de");
    bookingEto2.setBookingDate(new Timestamp(100L).toInstant());

    InvitedGuestEto guest = new InvitedGuestEto();
    guest.setEmail("tester3@test.com");
    ArrayList<InvitedGuestEto> guests = new ArrayList<>();
    guests.add(guest);

    BookingCto bookingCto = new BookingCto();
    bookingCto.setBooking(bookingEto2);
    bookingCto.setInvitedGuests(guests);
    BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
    OrderCto order = new OrderCto();
    order.setBooking(createdBooking);

    List<OrderLineCto> lines = new ArrayList<>();
    order.setOrderLines(lines);

    OrderEto createdOrder = this.orderManagement.saveOrder(order);
    assertThat(createdOrder.getInvitedGuestId()).isEqualTo(guests.get(0).getId());

  }

  /**
   * Tests if the status of an order can be changed
   */
  @Test
  public void updateStatus() {

    OrderEto createdOrder = this.orderManagement.saveOrder(this.orderCto);
    createdOrder.setOrderStatus("cancelled");
    OrderCto updatedStatus = this.orderManagement.updateOrderStatus(createdOrder);
    assertThat(updatedStatus.getOrderStatus()).isEqualTo("cancelled");
    this.orderManagement.deleteOrder(createdOrder.getId());
  }

  /**
   * Tests if the status of an order can be changed
   */
  @Test
  public void getTableId() {

    BookingEto bookingEto2 = new BookingEto();
    bookingEto2.setName("tester4");
    bookingEto2.setEmail("test4@test.de");
    bookingEto2.setBookingDate(new Timestamp(100L).toInstant());
    bookingEto2.setTableId(7L);

    BookingCto bookingCto = new BookingCto();
    bookingCto.setBooking(bookingEto2);

    BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
    OrderCto order = new OrderCto();
    order.setBooking(createdBooking);
    List<OrderLineCto> lines = new ArrayList<>();
    order.setOrderLines(lines);

    OrderEto createdOrder = this.orderManagement.saveOrder(order);
    assertThat(this.orderManagement.findOrder(createdOrder.getId()).getBooking().getTableId()).isEqualTo(7);
  }

  /**
   * Tests that an order with a wrong token is not created
   */
  @Test
  public void orderAnOrderWithWrongToken() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("wrongToken");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      WrongTokenException wte = new WrongTokenException();
      assertThat(e.getClass()).isEqualTo(wte.getClass());
    }
  }

  /**
   * Tests that an already created order is not created again
   */
  @Test
  public void orderAnOrderAlreadyCreated() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("CB_20170509_123502555Z");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      OrderAlreadyExistException oae = new OrderAlreadyExistException();
      assertThat(e.getClass()).isEqualTo(oae.getClass());
    }
  }

  /**
   * Tests that an order with a booking token that does not exist is not created
   */
  @Test
  public void orderAnOrderBookingNotExist() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("CB_Not_Existing_Token");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      NoBookingException nb = new NoBookingException();
      assertThat(e.getClass()).isEqualTo(nb.getClass());
    }
  }

  /**
   * Tests that an order with a guest token that does not exist is not created
   */
  @Test
  public void orderAnOrderInviteNotExist() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("GB_Not_Existing_Token");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      NoInviteException ni = new NoInviteException();
      assertThat(e.getClass()).isEqualTo(ni.getClass());
    }
  }

  @Test
  public void updatePaid() {

    BookingEto bookingEto2 = new BookingEto();
    bookingEto2.setBookingToken("CB_20170510_123502655Z");
    BookingCto bookingCto = new BookingCto();
    bookingCto.setBooking(bookingEto2);
    bookingEto2.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());
    bookingEto2.setName("tester5");
    bookingEto2.setEmail("tester5@mail.com");
    BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);

    // extra ingredients
    IngredientEto i1 = new IngredientEto();
    i1.setId(0L);
    IngredientEto i2 = new IngredientEto();
    i2.setId(1L);
    List<IngredientEto> extras = new ArrayList<>();
    extras.add(i1);
    extras.add(i2);

    // Dish
    DishEto dishEto = new DishEto();
    dishEto.setId(5L);

    // OrderLine Eto 1
    OrderLineEto olEto1 = new OrderLineEto();
    olEto1.setAmount(3);
    olEto1.setComment("This is a test order line");
    olEto1.setDishId(dishEto.getId());
    System.err.println("DISHID " + olEto1.getDishId().toString());

    // order line 1
    OrderLineCto ol1 = new OrderLineCto();
    ol1.setDish(dishEto);
    ol1.setOrderLine(olEto1);
    ol1.setExtras(extras);
    // ol1.setAmount();

    // OrderLineCto ol2 = new OrderLineCto();
    // ol2.setDish(dishEto);
    // ol1.setOrderLine(olEto2);

    // order
    List<OrderLineCto> lines = new ArrayList<>();
    lines.add(ol1);

    OrderCto orderCtoNew = new OrderCto();
    orderCtoNew.setBooking(createdBooking);
    orderCtoNew.setOrderLines(lines);
    orderCtoNew.setOrderStatus("open");
    orderCtoNew.setOrder(null);

    OrderEto createdOrder2 = this.orderManagement.saveOrder(orderCtoNew);
    createdOrder2.setPaid(true);
    this.orderManagement.updatePaid(createdOrder2);

    assertEquals(true, this.orderManagement.findOrder(createdOrder2.getId()).getOrder().getPaid());
  }

  @Test
  public void findOrdersByOrderStatus() {

    assertEquals(2, this.orderManagement.findOrdersByOrderStatus("delivered").size());
    BookingEto bookingEto2 = new BookingEto();
    bookingEto2.setName("tester6");
    bookingEto2.setEmail("test6@test.de");
    bookingEto2.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());

    InvitedGuestEto guest = new InvitedGuestEto();
    guest.setEmail("tester6@test.com");
    ArrayList<InvitedGuestEto> guests = new ArrayList<>();
    guests.add(guest);

    BookingCto bookingCto = new BookingCto();
    bookingCto.setBooking(bookingEto2);
    bookingCto.setInvitedGuests(guests);
    BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
    OrderCto order = new OrderCto();
    order.setBooking(createdBooking);
    order.setOrderStatus("delivered");

    List<OrderLineCto> lines = new ArrayList<>();
    order.setOrderLines(lines);

    OrderEto createdOrder = this.orderManagement.saveOrder(order);
    // System.err.println("LISTE: " + this.orderManagement.findOrdersByOrderStatus("delivered").toString());
    assertEquals(3, this.orderManagement.findOrdersByOrderStatus("delivered").size());

  }

  @Test
  public void findOrderedDishes() {

    OrderedDishesSearchCriteriaTo criteria = new OrderedDishesSearchCriteriaTo();
    criteria.setStartBookingdate(new Timestamp(System.currentTimeMillis()));
    criteria.setEndBookingdate(new Timestamp(2021, 8, 10, 11, 8, 32, 4));
    criteria.setType(Type.DAILY);
    Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
    criteria.setPageable(firstPageWithTwoElements.first());
    this.orderManagement.findOrderedDishes(criteria);
  }

  @Test
  public void findOrderLineCtos() {

    DishEto dish = new DishEto();
    dish.setId(4L);

    BookingEto bookingEto2 = new BookingEto();
    bookingEto2.setName("tester8");
    bookingEto2.setEmail("test8@test.de");
    bookingEto2.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());

    InvitedGuestEto guest = new InvitedGuestEto();
    guest.setEmail("tester8@test.com");
    ArrayList<InvitedGuestEto> guests = new ArrayList<>();
    guests.add(guest);

    BookingCto bookingCto = new BookingCto();
    bookingCto.setBooking(bookingEto2);
    bookingCto.setInvitedGuests(guests);
    BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
    OrderCto order = new OrderCto();
    order.setBooking(createdBooking);
    order.setOrderStatus("open");

    List<OrderLineCto> lines = new ArrayList<>();
    order.setOrderLines(lines);

    OrderEto createdOrder = this.orderManagement.saveOrder(order);

    OrderLineSearchCriteriaTo criteria = new OrderLineSearchCriteriaTo();
    criteria.setOrderId(createdOrder.getId());
    criteria.setDishId(dish.getId());
    criteria.setAmount(3);
    criteria.setComment("test");
    // criteria.setEndBookingdate(new Timestamp(2021, 8, 10, 11, 8, 32, 4));
    // criteria.setType(Type.DAILY);

    // Page<OrderLineEntity> orderLinePage = null;
    // List<OrderLineEntity> orderLineList = new ArrayList<OrderLineEntity>();
    // OrderLineEntity orderLineEntity = new OrderLineEntity();
    // OrderLineEntity.setId(Long.valueOf(1));
    // OrderLineEntity.setTimestamp(Integer.valueOf(5));
    // OrderLineEntity.setForecast(Double.valueOf(0.8));
    // OrderLineEntity.setDishName("Dish 1");
    // OrderLineEntity.setModificationCounter(0);
    // predictionDayDataList.add(predictionDayDataEntity);
    //
    // predictionDayDataPage = new PageImpl<PredictionDayDataEntity>(predictionDayDataList);
    // return predictionDayDataPage;

    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);
    this.orderManagement.findOrderLineCtos(criteria);
  }

  @Test
  public void findOrdersByInvitedGuests() {

    BookingEto bookingEto2 = new BookingEto();
    bookingEto2.setName("tester7");
    bookingEto2.setEmail("test7@test.de");
    bookingEto2.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());

    InvitedGuestEto guest = new InvitedGuestEto();
    guest.setEmail("tester7@test.com");
    guest.setGuestToken("GB_20170510_02350260000Z");
    guest.setBookingId(4L);
    guest.setAccepted(true);
    InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(guest);
    ArrayList<InvitedGuestEto> guests = new ArrayList<>();
    guests.add(invitedGuest);

    OrderCto order = new OrderCto();
    order.setOrderStatus("open");

    // Dish
    DishEto dishEto = new DishEto();
    dishEto.setId(5L);

    // OrderLine Eto 1
    OrderLineEto olEto1 = new OrderLineEto();
    olEto1.setAmount(3);
    olEto1.setComment("This is a test order line");
    olEto1.setDishId(dishEto.getId());

    OrderLineCto ol1 = new OrderLineCto();
    ol1.setDish(dishEto);
    ol1.setOrderLine(olEto1);

    List<OrderLineCto> lines = new ArrayList<>();
    lines.add(ol1);
    order.setOrderLines(lines);

    BookingCto bookingCto = new BookingCto();
    bookingCto.setBooking(bookingEto2);
    bookingCto.setInvitedGuests(guests);

    bookingCto.setOrder(null);
    BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);

    System.err.println("ID " + guest.getId());
    assertEquals(1, this.orderManagement.findOrdersByInvitedGuest(guest.getId()).size());

  }

  @Test
  public void findOrdersByBookingToken() {

    BookingEto bookingEto2 = new BookingEto();
    bookingEto2.setName("tester9");
    bookingEto2.setEmail("test9@test.de");
    bookingEto2.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());

    InvitedGuestEto guest = new InvitedGuestEto();
    guest.setEmail("tester9@test.com");
    guest.setGuestToken("GB_20170510_02350260099Z");
    guest.setBookingId(4L);
    InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(guest);
    ArrayList<InvitedGuestEto> guests = new ArrayList<>();
    guests.add(invitedGuest);

    BookingCto bookingCto = new BookingCto();
    bookingCto.setBooking(bookingEto2);
    bookingCto.setInvitedGuests(guests);
    BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
    OrderCto order = new OrderCto();
    order.setBooking(createdBooking);
    order.setOrderStatus("open");
    OrderEto oe = new OrderEto();
    order.setOrder(oe);

    List<OrderLineCto> lines = new ArrayList<>();
    order.setOrderLines(lines);

    OrderEto createdOrder = this.orderManagement.saveOrder(order);
    assertThat(createdOrder).isNotNull();
    System.err.println("1: " + createdOrder.getOrderStatus());
    System.err.println("1: " + createdOrder.getBookingToken());
    System.err.println("1: " + createdOrder.getId());
    assertEquals(1, this.orderManagement.findOrdersByBookingToken(createdOrder.getBookingToken()).size());

  }

  // @Test
  // public void findOrdersByBookingToken() {
  //
  // BookingEto bookingEto2 = new BookingEto();
  // bookingEto2.setName("tester9");
  // bookingEto2.setEmail("test9@test.de");
  // bookingEto2.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());
  //
  // InvitedGuestEto guest = new InvitedGuestEto();
  // guest.setEmail("tester9@test.com");
  // guest.setGuestToken("GB_20170510_02350260099Z");
  // guest.setBookingId(4L);
  // InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(guest);
  // ArrayList<InvitedGuestEto> guests = new ArrayList<>();
  // guests.add(invitedGuest);
  //
  // BookingCto bookingCto = new BookingCto();
  // bookingCto.setBooking(bookingEto2);
  // bookingCto.setInvitedGuests(guests);
  // BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
  // OrderCto order = new OrderCto();
  // order.setBooking(createdBooking);
  // order.setOrderStatus("open");
  //
  // // Dish
  // DishEto dishEto = new DishEto();
  // dishEto.setId(5L);
  //
  // // OrderLine Eto 1
  // OrderLineEto olEto1 = new OrderLineEto();
  // olEto1.setAmount(3);
  // olEto1.setComment("This is a test order line");
  // olEto1.setDishId(dishEto.getId());
  // // order line 1
  // OrderLineCto ol1 = new OrderLineCto();
  // ol1.setDish(dishEto);
  // ol1.setOrderLine(olEto1);
  //
  // List<OrderLineCto> lines = new ArrayList<>();
  // lines.add(ol1);
  // order.setOrderLines(lines);
  //
  // OrderEto createdOrder = this.orderManagement.saveOrder(order);
  // assertEquals(1, this.orderManagement.findOrdersByBookingToken(createdOrder.getBookingToken()).size());
  //
  // }

  // @Test
  // public void deleteOrderLine() {
  //
  // // extra ingredients
  // IngredientEto i1 = new IngredientEto();
  // i1.setId(5L);
  // IngredientEto i2 = new IngredientEto();
  // i2.setId(6L);
  // List<IngredientEto> extras = new ArrayList<>();
  // extras.add(i1);
  // extras.add(i2);
  //
  // // Dish
  // DishEto dishEto = new DishEto();
  // dishEto.setId(3L);
  //
  // BookingEto bookingEto2 = new BookingEto();
  // bookingEto2.setName("tester9");
  // bookingEto2.setEmail("test9@test.de");
  // bookingEto2.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());
  //
  // BookingCto bookingCto = new BookingCto();
  // bookingCto.setBooking(bookingEto2);
  //
  // BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
  //
  //
  //
  // OrderCto order = new OrderCto();
  // order.setBooking(createdBooking);
  // order.setOrderStatus("open");
  //
  // OrderEto createdOrder = this.orderManagement.saveOrder(order);
  //
  // // // extra ingredients
  // // IngredientEto i3 = new IngredientEto();
  // // i1.setId(7L);
  // // IngredientEto i4 = new IngredientEto();
  // // i2.setId(8L);
  // // List<IngredientEto> extras2 = new ArrayList<>();
  // // extras2.add(i3);
  // // extras2.add(i4);
  // //
  // // // Dish
  // // DishEto dishEto2 = new DishEto();
  // // dishEto.setId(4L);
  //
  // // OrderLine Eto 2
  // OrderLineEto olEto2 = new OrderLineEto();
  // olEto2.setAmount(6);
  // olEto2.setComment("test order line");
  // olEto2.setDishId(dishEto.getId());
  // olEto2.setOrderId(createdOrder.getId());
  //
  // // order line 2
  // // OrderLineCto ol2 = new OrderLineCto();
  // // ol2.setDish(dishEto);
  // // ol2.setOrderLine(olEto2);
  // // ol2.setExtras(extras);
  //
  // OrderLineEto orderLine = this.orderManagement.saveOrderLine(olEto2);
  //
  // assertThat(this.orderManagement.findOrderLine(orderLine.getId())).isNotNull();
  // this.orderManagement.deleteOrderLine(orderLine.getId());
  // assertThat(this.orderManagement.findOrderLine(orderLine.getId())).isNull();
  //
  // }

  // /**
  // * Tests if an order is created and values are identical
  // */
  // @Test
  // public void changeOrder() {
  //
  // BookingEto bookingEto2 = new BookingEto();
  // bookingEto2.setName("testerHans");
  // bookingEto2.setEmail("testHans@test.de");
  // bookingEto2.setBookingDate(new Timestamp(100L).toInstant());
  //
  // BookingCto bookingCto = new BookingCto();
  // bookingCto.setBooking(bookingEto2);
  //
  // BookingEto createdBooking = this.bookingManagement.saveBooking(bookingCto);
  // OrderCto order = new OrderCto();
  // order.setBooking(createdBooking);
  //
  // List<OrderLineCto> lines = new ArrayList<>();
  // order.setOrderLines(lines);
  //
  // OrderEto createdOrder = this.orderManagement.saveOrder(order);
  //
  // // Dish
  // DishEto dishEto5 = new DishEto();
  // dishEto5.setId(5L);
  //
  // // OrderLine Eto 1
  // OrderLineEto olEto1 = new OrderLineEto();
  // olEto1.setAmount(3);
  // olEto1.setComment("This is a test order line");
  // olEto1.setDishId(dishEto5.getId());
  //
  // // order line 1
  // OrderLineCto ol1 = new OrderLineCto();
  // ol1.setDish(dishEto5);
  // ol1.setOrderLine(olEto1);
  //
  // // order
  // List<OrderLineCto> lines2 = new ArrayList<>();
  // lines2.add(ol1);
  //
  // OrderCto newOrder = new OrderCto();
  // newOrder.setOrder(createdOrder);
  // newOrder.setOrderLines(lines2);
  // System.err.println(newOrder.getOrderLines().get(0).getDish().getId());
  // newOrder.setBooking(createdBooking);
  //
  // }

}