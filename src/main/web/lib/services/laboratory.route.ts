import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { LaboratoryService } from './laboratory.service';
import { Laboratory } from '../model/laboratory.model';
import { LaboratoryDetailsComponent } from '../components/laboratory-details.component';
import { LaboratoryEditComponent } from '../components/laboratory-edit.component';

@Injectable()
export class LaboratoryResolve implements Resolve<Laboratory> {
    constructor(private service: LaboratoryService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Laboratory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.findByUuid(id).pipe(
                filter((response: HttpResponse<Laboratory>) => response.ok),
                map((patient: HttpResponse<Laboratory>) => patient.body)
            );
        }
        return of(<Laboratory>{});
    }
}

export const ROUTES: Routes = [
    {
        path: '',
        data: {
            title: 'Laboratory Request',
            breadcrumb: 'LABORATORY REQUEST'
        },
        children: [
            {
                path: ':id/patient/:patientId/view',
                component: LaboratoryDetailsComponent,
                resolve: {
                    entity: LaboratoryResolve
                },
                data: {
                    authorities: ['ROLE_USER'],
                    title: 'Laboratory Request',
                    breadcrumb: 'LABORATORY REQUEST'
                },
                //canActivate: [UserRouteAccessService]
            },
            {
                path: 'patient/:patientId/new',
                component: LaboratoryEditComponent,
                data: {
                    authorities: ['ROLE_DEC'],
                    title: 'LABORATORY REQUEST',
                    breadcrumb: 'ADD LABORATORY REQUEST'
                },
                //canActivate: [UserRouteAccessService]
            },
            {
                path: ':id/patient/:patientId/edit',
                component: LaboratoryEditComponent,
                resolve: {
                    entity: LaboratoryResolve
                },
                data: {
                    authorities: ['ROLE_DEC'],
                    title: 'Laboratory Request Edit',
                    breadcrumb: 'LABORATORY REQUEST EDIT'
                },
                //canActivate: [UserRouteAccessService]
            }
        ]
    }
];

