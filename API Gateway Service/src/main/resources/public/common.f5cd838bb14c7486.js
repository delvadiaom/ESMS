"use strict";(self.webpackChunksidenav=self.webpackChunksidenav||[]).push([[592],{222:(C,d,r)=>{r.d(d,{W:()=>_});var n=r(4650),c=r(6895),i=r(9299);let s=(()=>{class a{transform(t){return t>=1e9?(t/1e8).toFixed(2)+" B":t>=1e6?(t/1e5).toFixed(2)+" M":t>=1e3?(t/1e3).toFixed(2)+" K":t}}return a.\u0275fac=function(t){return new(t||a)},a.\u0275pipe=n.Yjl({name:"currencyConverter",type:a,pure:!0}),a})();function p(a,e){if(1&a&&(n.TgZ(0,"div",8),n._UZ(1,"i"),n.qZA()),2&a){const t=n.oxw();n.xp6(1),n.Tol(null==t.cardData?null:t.cardData.icon)}}function l(a,e){if(1&a&&(n.TgZ(0,"span",9),n._uU(1," Lead By : "),n.TgZ(2,"span"),n._uU(3),n.ALo(4,"titlecase"),n.qZA()()),2&a){const t=n.oxw();n.xp6(3),n.hij(" ",n.lcZ(4,1,null==t.cardData?null:t.cardData.leadByName),"")}}function f(a,e){if(1&a&&(n.TgZ(0,"span",10),n._uU(1),n.qZA()),2&a){const t=n.oxw();n.xp6(1),n.hij(" ",t.cardData.data||t.cardData.employeeCount||0,"")}}function g(a,e){if(1&a&&(n.TgZ(0,"span",10),n._uU(1),n.ALo(2,"currencyConverter"),n.qZA()),2&a){const t=n.oxw();n.xp6(1),n.hij(" \u20b9 ",n.lcZ(2,1,(null==t.cardData?null:t.cardData.data)||0),"")}}let _=(()=>{class a{}return a.\u0275fac=function(t){return new(t||a)},a.\u0275cmp=n.Xpm({type:a,selectors:[["app-main-card"]],inputs:{cardData:"cardData",cardRoute:"cardRoute"},decls:11,vars:9,consts:[[1,"c-dashboardInfo",3,"routerLink"],[1,"wrap"],["class","icon",4,"ngIf"],[3,"ngClass"],[1,"c-dashboardInfo__title"],["class","c-dashboardInfo__leadByName",4,"ngIf"],["class","c-dashboardInfo__count",4,"ngIf"],[1,"progressBar"],[1,"icon"],[1,"c-dashboardInfo__leadByName"],[1,"c-dashboardInfo__count"]],template:function(t,o){1&t&&(n.TgZ(0,"div",0)(1,"div",1),n.YNc(2,p,2,2,"div",2),n.TgZ(3,"div",3)(4,"h4",4),n._uU(5),n.ALo(6,"titlecase"),n.qZA(),n.YNc(7,l,5,3,"span",5),n.YNc(8,f,2,1,"span",6),n.YNc(9,g,3,3,"span",6),n.qZA(),n._UZ(10,"div",7),n.qZA()()),2&t&&(n.Q6J("routerLink",o.cardRoute),n.xp6(2),n.Q6J("ngIf",null==o.cardData?null:o.cardData.icon),n.xp6(1),n.Q6J("ngClass",null!=o.cardData&&o.cardData.icon?"data":"withoutIconData"),n.xp6(2),n.hij(" ",n.lcZ(6,7,(null==o.cardData?null:o.cardData.title)||(null==o.cardData?null:o.cardData.teamName))," "),n.xp6(2),n.Q6J("ngIf",null==o.cardData?null:o.cardData.leadByName),n.xp6(1),n.Q6J("ngIf",!o.cardData.itsCurrency),n.xp6(1),n.Q6J("ngIf",null==o.cardData?null:o.cardData.itsCurrency))},dependencies:[c.mk,c.O5,i.rH,c.rS,s],styles:['.c-dashboardInfo[_ngcontent-%COMP%]{width:100%;min-width:250px;margin-bottom:50px}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]{background:#ffffff;box-shadow:2px 10px 20px #0000001a;border-radius:10px;display:flex;align-items:center;position:relative;padding:40px 25px 20px;overflow:hidden;height:100%;transition:.3s}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   h4[_ngcontent-%COMP%]{margin:0}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   .icon[_ngcontent-%COMP%]{margin-top:-15px;padding:10px 15px 15px;width:auto;height:50px;background-color:#27367f;border-radius:5px;margin-right:30px}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   i[_ngcontent-%COMP%]{color:#fff;font-size:30px}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   .progressBar[_ngcontent-%COMP%]{position:absolute;bottom:0;left:0;width:0;height:10px;content:"";transition:.3s}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]:hover{transform:translateY(-5px)}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]:hover   .progressBar[_ngcontent-%COMP%]{width:100%}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   .withoutIconData[_ngcontent-%COMP%]{background:#ffffff;border-radius:10px;text-align:center;display:flex;flex-direction:column;align-items:center;position:relative;overflow:hidden;height:100%;width:100%;transition:.3s}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   .c-dashboardInfo__title[_ngcontent-%COMP%]{color:#6c6c6c;font-size:1.18em}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   .c-dashboardInfo[_ngcontent-%COMP%]   span[_ngcontent-%COMP%]{display:block}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   .c-dashboardInfo__leadByName[_ngcontent-%COMP%]{font-weight:700;margin-top:10px;color:#6c6c6c}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   .c-dashboardInfo__leadByName[_ngcontent-%COMP%]   span[_ngcontent-%COMP%]{font-weight:300}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]   .c-dashboardInfo__count[_ngcontent-%COMP%]{font-weight:600;font-size:2.5em;line-height:64px;color:#323c43}.c-dashboardInfo[_ngcontent-%COMP%]   .wrap[_ngcontent-%COMP%]:before{display:block;position:absolute;bottom:0;left:0;width:100%;height:10px;content:""}.c-dashboardInfo[_ngcontent-%COMP%]:hover{cursor:pointer}']}),a})()}}]);