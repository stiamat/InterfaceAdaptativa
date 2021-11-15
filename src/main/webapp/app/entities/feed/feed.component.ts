import { HttpResponse } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { Router } from "@angular/router";
import { AccountService } from "app/core/auth/account.service";
import { LoginService } from "app/core/login/login.service";
import { IPost, Post } from "app/shared/model/post.model";
import * as moment from 'moment';
import { Observable } from "rxjs";
import { PostService } from "../post/post.service";

function delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
}

@Component({
    selector: 'jhi-feed',
    templateUrl: './feed.component.html',
    styleUrls: ['feed.component.scss']
})
export class FeedComponent implements OnInit {
    input = "";
    loading = true;
    post:IPost = {id:0};
    feed = [this.post,this.post,this.post];
    account: any = null;
    isSaving = false;

    editForm = this.fb.group({
        id: [],
        body: [],
        date: [],
        active: [],
        likes: [],
        userId: [],
    });

    constructor(
        private postService: PostService,
        private loginService: LoginService,
        private router: Router,
        private fb: FormBuilder,
        private accountService: AccountService
    ) {};

    ngOnInit(): void {
        
        (async () => { 
            await delay(3000);
            this.loadAll();
        })();

        this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    }

    loadAll(): void {
        this.postService
          .query({
            'active.equals':true,
            page: 0,
            size: 999,
            sort: ['id,desc'],
          })
          .subscribe(
            (res: HttpResponse<IPost[]>) => {
                this.loading = false;
                this.feed = res.body;
                console.log(res.body);
            });
    }

    private createFromForm(): IPost {
        return {
          ...new Post(),
          id: null,
          body: this.input,
          date: moment(moment.now()),
          active: true,
          likes: 0,
          userId: 3,
        };
    }

    createPost(){
        this.isSaving = true;
        const post = this.createFromForm();
        this.subscribeToSaveResponse(this.postService.create(post));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPost>>): void {
        result.subscribe(
          () => this.onSaveSuccess(),
          () => this.onSaveError()
        );
      }
    
      protected onSaveSuccess(): void {
        this.isSaving = false;
        this.input = null;
        this.loadAll();
      }
    
      protected onSaveError(): void {
        this.isSaving = false;
      }

    checkinput(any: any):void{
        // console.warn(this.input.length);
    }

    logout(){
        this.loginService.logout();
        this.router.navigate(['']);
    }
}

