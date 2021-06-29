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
  ResetTokenView
} from '../../shared/view-models/interfaces';
import {MatSnackBar} from '@angular/material/snack-bar';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';


@Injectable()
export class AdminCockpitService {
  private readonly getUserRestPath: string =
    'usermanagement/v1/user/search';
  private readonly getUserroleRestPath: string =
    'usermanagement/v1/userrole/search';
  private readonly deleteUserRestPath: string =
    'usermanagement/v1/user';
  private readonly addUserRestPath: string =
    'usermanagement/v1/user';
  private readonly resetPasswordMailRestPath: string =
    'usermanagement/v1/resetPassword';
  private readonly changePasswordRestPath: string =
    'usermanagement/v1/changePassword';
  private readonly tokenRestPath: string =
    'usermanagement/v1/token';

  private readonly restServiceRoot$: Observable<
    string
  > = this.config.getRestServiceRoot();

  constructor(
    private http: HttpClient,
    private config: ConfigService,
    private _snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute,
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
  }

  sendPasswordResetMail(userId: number): Observable<{}> {
    let path = this.resetPasswordMailRestPath;
    return  this.restServiceRoot$.pipe(
            exhaustMap((restServiceRoot) =>
            this.http.get(`${restServiceRoot}${path}/${userId}`),
      ),
    );
  }


  getUserIdByToken(token: String): Observable<{}> {
    let path = this.tokenRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.get(`${restServiceRoot}${path}/${token}`)
      )
    );
  }

  // changePassword(userId: number) {
  //   let path = this.addUserRestPath;
  // }

  // delete certain user from database by id
  deleteUser(userId: number): Observable<{}> {
    let path = this.deleteUserRestPath;
    let result= this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.delete(`${restServiceRoot}${path}/${userId}`),
      ),
    );
    return result;
  }

  // admin can add a user
  addUser(username: string, email: string, userRoleId: number, password: string) {
    let path = this.addUserRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post(`${restServiceRoot}${path}`, {
          username: username,
          email: email,
          userRoleId: userRoleId,
          password: password
        }),
      ),
    );
  }

  changePassword(idUser: number, password: String, token: String){
    let path = this.changePasswordRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post(`${restServiceRoot}${path}`,
          {
          userId: idUser,
          password: password,
          token: token
          }
        ),
      ),
    );
  }

  snackBar(msg: string, res: string){
    this._snackBar.open(msg, res);
  }

  // reloads the page
  async reloadPage(url: string): Promise<boolean> {
    await this.router.navigateByUrl('/restaurant', { skipLocationChange: true });
    return this.router.navigateByUrl(url);
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
