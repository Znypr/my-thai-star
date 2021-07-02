// FILTERS

export class Filter {
  pageable?: Pageable;
  isFav: boolean;
  searchBy: string;
  // sort: { name: string, direction: string }[];
  maxPrice: number;
  minLikes: number;
  categories: { id: string }[];
}

export class FilterCockpit {
  pageable?: Pageable;
  // sort?: Sorting[];
  bookingDate: string;
  email: string;
  bookingToken: number;
  orderStatus?: string;
  paid?: boolean;
}

// to filter users in admin-cockpit
export class FilterAdminCockpit {
  pageable?: Pageable;
  id: number;
  username: string;
  email: string;
  idRole: number;
}

export class FilterOrdersCockpit {
  pageable?: Pageable;
  // sort?: Sorting[];
  type: string;
  startBookingdate: string;
  endBookingdate: string;
}

export class Pageable {
  pageSize: number;
  pageNumber: number;
  sort?: Sort[];
}

export class Sort {
  property: string;
  direction: string;
}

// DISHES
export class ExtraInfo {
  id: number;
  name: string;
  price: number;
  selected: boolean;
}

// BOOKING
export class BookingInfo {
  booking: ReservationInfo;
  invitedGuests?: {
    [index: number]: { email: string };
  };
}

export class ReservationInfo {
  bookingDate: string;
  name: string;
  email: string;
  bookingType: number;
  assistants?: number;
}

export class FriendsInvite {
  email: string;
  acceptance: string;
}

export class OrderInfo {
  orderLine: OrderLineInfo;
  extras: number[];
}

export class OrderLineInfo {
  dishId: number;
  amount: number;
  comment: string;
}

export class OrderListInfo {
  booking: { bookingToken: string };
  orderLines: OrderInfo[];
}

export class PredictionCriteria {
  pageable?: Pageable;
  type: string;
  startBookingdate: string;
  temperatures: number[];
  holidays: string[];
}

export class ClusteringCriteria {
  startBookingdate: string;
  endBookingdate: string;
  dishId: number;
  clusters: number;
}

// LOGIN
export class LoginInfo {
  username: string;
  password: string;
  role: string;
  token?: string;
}

export class Role {
  name: string;
  permission: number;
}
