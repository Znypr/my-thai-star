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
  userId: number;
  isValid= false;
  resetTokenEntity: any;

  constructor(
   private route: ActivatedRoute,
   private router: Router,
   private adminCockpitService: AdminCockpitService
  ) { }


  ngOnInit(): void {
    this.route.queryParamMap.subscribe(
      (params) => {
        this.token = params.get('token');
        this.userId = Number(params.get('id'));
      }
    );
  }

  getTokenByToken(token: String) {
    // let id;
    // this.adminCockpitService.getUserIdByToken(token).subscribe(
    //   (data: any) => {
    //     id= data;
    //   }
    // );
    // console.log(id);
  }

  onButtonClick(token: String){
    this.adminCockpitService.getUserIdByToken(token).subscribe(
      (data: any) => {
        if (!data) {
          this.resetTokenEntity = [];
        } else {
          this.resetTokenEntity = data;
        }
    });
    // console.log(this.entity.content);
  }

  changePassword(event: any){
    const info = [
      event.target.Password.value,
      event.target.confirmPassword.value,
    ];
    if(info[0]==info[1]){
      this.adminCockpitService.changePassword(this.userId,info[0],this.token).subscribe(
        () => this.adminCockpitService.snackBar("Passwort wurde geändert","verstanden")
      );
    } else {
      this.adminCockpitService.snackBar("Fragen Sie einen neuen Rücksetzlink an", "verstanden");
    }
  }

    // this.route.snapshot.paramMap.get('token');

}
