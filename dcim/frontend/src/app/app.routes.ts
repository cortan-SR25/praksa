import { Routes } from '@angular/router'; import { authGuard,adminGuard } from './core/auth.guard';
import { LoginComponent } from './pages/login.component'; import { DashboardComponent } from './pages/dashboard.component';
import { DevicesComponent } from './pages/devices.component'; import { LicensesComponent } from './pages/licenses.component'; import { UsersComponent } from './pages/users.component';
import { OrganizationComponent } from './pages/organization.component'; import { SoftwareComponent } from './pages/software.component'; import { InstallationsComponent } from './pages/installations.component';
export const routes:Routes=[
 {path:'login',component:LoginComponent},
 {path:'',component:DashboardComponent,canActivate:[authGuard]},
 {path:'devices',component:DevicesComponent,canActivate:[authGuard]},
 {path:'devices/:id/software',component:InstallationsComponent,canActivate:[authGuard]},
 {path:'licenses',component:LicensesComponent,canActivate:[authGuard]},
 {path:'users',component:UsersComponent,canActivate:[authGuard,adminGuard]},
 {path:'organization',component:OrganizationComponent,canActivate:[authGuard,adminGuard]},
 {path:'software',component:SoftwareComponent,canActivate:[authGuard,adminGuard]},
 {path:'**',redirectTo:''}
];
