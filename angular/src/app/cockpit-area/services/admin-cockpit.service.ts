import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { exhaustMap } from 'rxjs/operators';
import { ConfigService } from '../../core/config/config.service';
import { cloneDeep, map } from 'lodash';
import {
  FilterAdminCockpit,
  Pageable,
  Sort,
} from 'app/shared/backend-models/interfaces';
import {
  UserView,
  UserResponse,
} from '../../shared/view-models/interfaces';
import {MatSnackBar} from '@angular/material/snack-bar';


@Injectable()
export class AdminCockpitService {
  private readonly getUserRestPath: string =
    'usermanagement/v1/user/search';
  private readonly getUserroleRestPath: string =
    'usermanagement/v1/userrole/search';
  private readonly deleteUserRestPath: string =
    'usermanagement/v1/user';

  private readonly restServiceRoot$: Observable<
    string
  > = this.config.getRestServiceRoot();

  constructor(
    private http: HttpClient,
    private config: ConfigService,
    private _snackBar: MatSnackBar,
  ) { }

// get all Users from database
  getUsers(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterAdminCockpit,
  ): Observable<UserResponse[]> {
    let path: string;
    filters.pageable = pageable;
    filters.pageable.sort = sorting;
    if (filters.idRole){
          path= this.getUserroleRestPath;
    } else {
      delete filters.idRole;
      path= this.getUserRestPath;
    }
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<UserResponse[]>(`${restServiceRoot}${path}`, filters),
      ),
    );
  };

// delete certain user from database by id
  deleteUser(userId: number): Observable<{}> {
    let path = this.deleteUserRestPath;
    this._snackBar.open("gelöscht", "ok");
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.delete(`${restServiceRoot}${path}/${userId}`),
      ),
    );
    }

  userComposer(userList: UserView[]): UserView[] {
    const users: UserView[] = cloneDeep(userList);
    // map(users, (u: UserViewResult) => {
    //   o.users.id = this.priceCalculator.getPrice(o);
    //   o.extras = map(o.extras, 'name').join(', ');
    // });
    return users;
  }

}
