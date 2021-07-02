package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.TableEto;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.InvitedGuestEntity;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;

/**
 * TODO J This type ...
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class BookingmanagementTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingManagement;

  @Inject
  private Ordermanagement orderManagement;

  private static Instant instant = Instant.now();

  private static Timestamp timestamp = new Timestamp(100L);

  BookingCto bookingCto;

  BookingCto bookingCto3;

  BookingCto bookingCto4;

  BookingEto createdBookingEto;

  BookingEto bookingEto5;

  BookingCto bookingCto5;

  BookingEto createdBookingEto5;

  BookingEntity bookingEntity;

  InvitedGuestEto invitedGuestEto;

  InvitedGuestEto invitedGuestEto2;

  InvitedGuestEto invitedGuestEto3;

  InvitedGuestEto invitedGuestEto4;

  InvitedGuestEto createdInvitedGuest;

  InvitedGuestEntity invitedGuestEntity;

  TableEto tableEto;

  BookingSearchCriteriaTo criteria;

  List<InvitedGuestEto> invitedGuests;

  OrderEto orderEto;

  OrderEto createdOrderEto;

  OrderCto orderCto;

  @Override
  public void doSetUp() {

    super.doSetUp();

    this.invitedGuestEto = new InvitedGuestEto();
    this.invitedGuestEto.setBookingId(4L);
    this.invitedGuestEto.setEmail("test@mail.com");
    this.invitedGuestEto.setGuestToken("GB_20170510_02350260000Z");

    this.invitedGuestEto2 = new InvitedGuestEto();
    this.invitedGuestEto2.setBookingId(2L);
    this.invitedGuestEto2.setEmail("test2@mail.com");
    this.invitedGuestEto2.setGuestToken("GB_20170510_02350260002Z");

    this.invitedGuestEto3 = new InvitedGuestEto();
    this.invitedGuestEto3.setBookingId(2L);
    this.invitedGuestEto3.setEmail("test3@mail.com");
    this.invitedGuestEto3.setGuestToken("GB_20170510_02350260003Z");

    this.invitedGuestEto4 = new InvitedGuestEto();
    this.invitedGuestEto4.setBookingId(2L);
    this.invitedGuestEto4.setEmail("test4@mail.com");
    this.invitedGuestEto4.setGuestToken("GB_20170510_02350260004Z");
    //
    this.createdInvitedGuest = this.bookingManagement.saveInvitedGuest(this.invitedGuestEto);

    this.invitedGuests = new ArrayList<InvitedGuestEto>();
    this.invitedGuests.add(this.createdInvitedGuest);

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("CB_20170510_123502111Z");
    bookingEto.setName("tester");
    bookingEto.setEmail("test@mail.com");

    this.bookingEntity = new BookingEntity();
    this.bookingEntity.setEmail("test@mail.com");
    this.bookingEntity.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());
    this.bookingEntity.setBookingToken("CB_20170510_123502111Z");

    bookingEto.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());

    this.bookingCto = new BookingCto();
    this.bookingCto.setBooking(bookingEto);
    this.bookingCto.setInvitedGuests(this.invitedGuests);

    BookingEto bookingEto3 = new BookingEto();
    bookingEto3.setBookingToken("CB_20170510_123502112Z");
    bookingEto3.setName("tester2");
    bookingEto3.setEmail("test2@mail.com");
    bookingEto3.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());

    this.bookingCto3 = new BookingCto();
    this.bookingCto3.setBooking(bookingEto3);

    this.tableEto = new TableEto();
    this.tableEto.setSeatsNumber(1);

    // BookingEto
    BookingEto bookingEto4 = new BookingEto();
    bookingEto4.setBookingToken("CB_20170510_123502114Z");
    bookingEto4.setName("tester4");
    bookingEto4.setEmail("test4@mail.com");
    bookingEto4.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());

    this.bookingCto4 = new BookingCto();
    this.bookingCto4.setBooking(bookingEto4);

    // createdBookingEto
    this.createdBookingEto = this.bookingManagement.saveBooking(this.bookingCto4);

  }

  /**
   * Tests whether a booking is created in the database
   */
  @Test
  public void saveBooking() {

    BookingEto createdBooking = this.bookingManagement.saveBooking(this.bookingCto);
    assertEquals("tester", createdBooking.getName());
  }

  /**
   * Tests whether invited friends are saved in the database
   */
  @Test
  public void saveInvitedGuest() {

    InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(this.invitedGuestEto);
    assertEquals(4, invitedGuest.getBookingId());
    assertEquals("test@mail.com", invitedGuest.getEmail());

  }

  /**
   * Tests whether booked tables are saved in the database
   */
  @Test
  public void saveTable() {

    TableEto bookedTable = this.bookingManagement.saveTable(this.tableEto);
    assertEquals(this.bookingManagement.findTable(bookedTable.getId()).getId(), bookedTable.getId());
    assertEquals(1, bookedTable.getSeatsNumber());
  }

  /**
   * Test whether accepted attribute is true if invitation was accepted
   */
  @Test
  public void acceptInvite() {

    // InvitedGuestEto invitedGuestEto = new InvitedGuestEto();
    // invitedGuestEto.setBookingId(2L);
    // invitedGuestEto.setEmail("test7@mail.com");
    // invitedGuestEto.setGuestToken("GB_20170510_02350260055Z");
    //
    // InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(invitedGuestEto);
    InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(this.invitedGuestEto4);

    InvitedGuestEto accepted = this.bookingManagement.acceptInvite(invitedGuest.getGuestToken());
    assertEquals(true, accepted.getAccepted());
    //
    // InvitedGuestEto accepted = this.bookingManagement.acceptInvite(invitedGuest.getGuestToken());
    // assertEquals(true, accepted.getAccepted());

  }

  /**
   * Test whether accepted attribute is false if invitation was declined
   */
  @Test
  public void declineInvite() {

    // InvitedGuestEto invited = this.bookingManagement.findInvitedGuestByToken("GB_20170510_02350260000Z");
    // System.out.println("AUSGABE: " + invited.getId());

    InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(this.invitedGuestEto2);

    InvitedGuestEto declined = this.bookingManagement.declineInvite(invitedGuest.getGuestToken());
    assertEquals(false, declined.getAccepted());
  }

  /**
   * Check whether the booking is deleted after the invitation is canceled
   */
  @Test
  public void cancelInvite() {

    BookingEto bookingEto9 = new BookingEto();
    bookingEto9.setBookingToken("CB_20170510_123502122Z");
    bookingEto9.setName("tester8");
    bookingEto9.setEmail("test8@mail.com");
    bookingEto9.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());
    System.err.println("TIME:  " + bookingEto9.getBookingDate());

    BookingCto bookingCto9 = new BookingCto();
    bookingCto9.setBooking(bookingEto9);
    bookingCto9.setInvitedGuests(this.invitedGuests);

    // createdBookingEto
    BookingEto createdBookingEto = this.bookingManagement.saveBooking(bookingCto9);

    String token = createdBookingEto.getBookingToken();
    assertThat(this.bookingManagement.findBookingByToken(token)).isNotNull();
    this.bookingManagement.cancelInvite(token);
    assertThat(this.bookingManagement.findBookingByToken(token)).isNull();

  }

  @Test
  public void findBookingByToken() {

    BookingEto createdBooking3 = this.bookingManagement.saveBooking(this.bookingCto3);

    BookingCto cto = this.bookingManagement.findBookingByToken(createdBooking3.getBookingToken());
    assertEquals(cto.getOrder(),
        this.bookingManagement.findBookingByToken(createdBooking3.getBookingToken()).getOrder());

  }

  @Test
  public void deleteBooking() {

    this.bookingEto5 = new BookingEto();
    this.bookingEto5.setBookingToken("CB_20170510_123502115Z");
    this.bookingEto5.setName("tester5");
    this.bookingEto5.setEmail("test5@mail.com");
    this.bookingEto5.setBookingDate(Timestamp.from(instant.plusMillis(4000000)).toInstant());

    // this.orderEto = new OrderEto();
    // this.orderEto.setOrderStatus("open");
    // this.orderCto = new OrderCto();
    // this.orderCto.setBooking(this.bookingEto5);
    // this.orderCto.setOrderStatus("open");
    // this.orderCto.setOrderLines(lines);
    // this.createdOrderEto = this.orderManagement.saveOrder(this.orderCto);

    // this.bookingEto5.setOrderId(this.createdOrderEto.getId());

    this.bookingCto5 = new BookingCto();
    this.bookingCto5.setBooking(this.bookingEto5);

    this.createdBookingEto5 = this.bookingManagement.saveBooking(this.bookingCto5);

    assertThat(this.bookingManagement.findBookingByToken(this.createdBookingEto5.getBookingToken())).isNotNull();
    this.bookingManagement.deleteBooking(this.createdBookingEto5.getId());
    assertThat(this.bookingManagement.findBookingByToken(this.createdBookingEto5.getBookingToken())).isNull();
  }

  @Test
  public void deleteInvitedGuest() {

    InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(this.invitedGuestEto3);
    assertThat(this.bookingManagement.findInvitedGuestByToken(invitedGuest.getGuestToken())).isNotNull();
    this.bookingManagement.deleteInvitedGuest(invitedGuest.getId());
    assertThat(this.bookingManagement.findInvitedGuestByToken(invitedGuest.getGuestToken())).isNull();

  }

  @Test
  public void findInvitedGuest() {

    InvitedGuestEto invitedGuest = this.bookingManagement.saveInvitedGuest(this.invitedGuestEto3);
    assertEquals(invitedGuest.getId(), this.bookingManagement.findInvitedGuest(invitedGuest.getId()).getId());
  }

  @Test
  public void deleteTable() {

    TableEto bookedTable = this.bookingManagement.saveTable(this.tableEto);
    assertThat(this.bookingManagement.findTable(bookedTable.getId())).isNotNull();
    this.bookingManagement.deleteTable(bookedTable.getId());
    assertThrows(EmptyResultDataAccessException.class, () -> this.bookingManagement.findTable(bookedTable.getId()));

  }

}