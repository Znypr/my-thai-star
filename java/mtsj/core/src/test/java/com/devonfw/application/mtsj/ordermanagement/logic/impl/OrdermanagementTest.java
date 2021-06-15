package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;

/**
 * Test for {@link Ordermanagement}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class OrdermanagementTest extends ApplicationComponentTest {

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

    // needs to change setBooking

    // BookingEto bookingEto2 = new BookingEto();
    // bookingEto2.setBookingToken("CB_20170510_123502567Z");
    // this.orderCto2 = new OrderCto();
    // this.orderCto2.setBooking(bookingEto2);
    // this.orderCto2.setOrderLines(lines);

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
    this.orderManagement.updateOrderStatus(createdOrder);
    assertThat(createdOrder.getOrderStatus()).isEqualTo("cancelled");
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
}
