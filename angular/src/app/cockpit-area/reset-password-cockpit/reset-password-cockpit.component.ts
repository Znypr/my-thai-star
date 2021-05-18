import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { AdminCockpitService } from '../services/admin-cockpit.service';

@Component({
  selector: 'app-reset-password-cockpit',
  templateUrl: './reset-password-cockpit.component.html',
  styleUrls: ['./reset-password-cockpit.component.scss']
})
export class ResetPasswordCockpitComponent implements OnInit {
  hide = true;
  token: String;
  isValid= false;
  resetTokenEntity: any;

  constructor(
   private route: ActivatedRoute,
   private router: Router,
   private adminCockpitService: AdminCockpitService
  ) { }

  // changePassword(userId: number) {
  //   let path = this.changePasswordRestPath;
  //   this.adminCockpitService.restServiceRoot$.pipe(
  //     exhaustMap((restServiceRoot) =>
  //       this.http.post(`${restServiceRoot}${path}`, {})
  // }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(
      (params) => {
        this.token = params.get('token');
        console.log("Token: ", params);
      }
    );
  }

  getTokenByToken(token: String) {
    this.adminCockpitService.getTokenByToken(token).subscribe(
      (data: any) => {
        if (!data) {
          this.resetTokenEntity = [];
        } else {
          this.resetTokenEntity = data;
        }
      }
    );
    console.log("token: " ,this.resetTokenEntity.id);
  }

  onButtonClick(token: String){
    this.adminCockpitService.getTokenByToken(token).subscribe(
      (data: any) => {
        if (!data) {
          this.resetTokenEntity = [];
        } else {
          this.resetTokenEntity = data;
        }
    });
    // console.log(this.entity.content);
  }

    // this.route.snapshot.paramMap.get('token');

}
