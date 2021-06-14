package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.TableEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * TODO J This type ...
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class BookingmanagementTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingManagement;

  BookingCto bookingCto;

  InvitedGuestEto invitedGuestEto;

  TableEto tableEto;

  @Override
  public void doSetUp() {

    super.doSetUp();

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("CB_20170510_123502111Z");
    bookingEto.setName("tester");
    bookingEto.setEmail("test@mail.com");

    Timestamp timestamp = new Timestamp(100L);

    bookingEto.setBookingDate(timestamp.toInstant());

    this.bookingCto = new BookingCto();
    this.bookingCto.setBooking(bookingEto);
    // this.bookingCto.se

    this.invitedGuestEto = new InvitedGuestEto();
    this.invitedGuestEto.setBookingId(4L);
    this.invitedGuestEto.setEmail("test@mail.com");
    this.invitedGuestEto.setGuestToken("GB_20170510_02350260000Z");

    this.tableEto = new TableEto();
    this.tableEto.setSeatsNumber(1);

  }

  /**
   * Tests whether a booking is created in the database
   */
  @Test
  public void saveBooking() {

    BookingEto createdBooking = this.bookingManagement.saveBooking(this.bookingCto);
    assertEquals("tester", createdBooking.getName());
    // assertThat(createdBooking).isNotNull();
    // assertThat(this.orderCto.getOrderLines().get(0).getDish()).isNotNull();
    // System.err.println(this.orderCto.getOrderLines().get(0).getExtras());
    // assertThat(this.orderCto.getOrderLines().get(0).getExtras()).is(null);
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

    InvitedGuestEto accepted = this.bookingManagement.acceptInvite("GB_20170510_02350260000Z");
    assertEquals(true, accepted.getAccepted());
  }

  /**
   * Test whether accepted attribute is false if invitation was declined
   */
  public void declineInvite() {

    InvitedGuestEto accepted = this.bookingManagement.declineInvite("GB_20170510_02350260000Z");
    assertEquals(false, accepted.getAccepted());
  }
}
