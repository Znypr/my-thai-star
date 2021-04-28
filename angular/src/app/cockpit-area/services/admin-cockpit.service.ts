import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { exhaustMap } from 'rxjs/operators';
import { ConfigService } from '../../core/config/config.service';
import {
  FilterAdminCockpit,
  Pageable,
  Sort,
} from 'app/shared/backend-models/interfaces';
import {
  UserView,
  UserResponse,
} from '../../shared/view-models/interfaces';


@Injectable()
export class AdminCockpitService {
  private readonly getUserRestPath: string =
    'usermanagement/v1/user/search';
  private readonly getUserroleRestPath: string =
    'usermanagement/v1/userrole/search';

  private readonly restServiceRoot$: Observable<
    string
  > = this.config.getRestServiceRoot();

  constructor(
    private http: HttpClient,
    private config: ConfigService,
  ) { }

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

}
