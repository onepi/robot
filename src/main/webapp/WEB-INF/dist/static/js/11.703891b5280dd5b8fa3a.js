webpackJsonp([11],{"3wLc":function(t,e){},sST6:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=i("vmSr"),a={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{attrs:{id:"exhibition-default"}},[i("div",{staticClass:"box1-left"},[i("currentLocate"),t._v(" "),i("div",{staticClass:"box3-left"},[i("keep-alive",[i("pb-list",{attrs:{widgetMessage:t._f("FormatDate")(t.tabs,"展会动态"),widgetType:2}})],1)],1),t._v(" "),i("div",{staticClass:"box3-left"},[t._m(0),t._v(" "),i("keep-alive",[i("mainListMeeting",{attrs:{queryProp:t.tab1}})],1)],1),t._v(" "),i("div",{staticClass:"box3-left"},[t._m(1),t._v(" "),i("keep-alive",[i("mainListMeeting",{attrs:{queryProp:t.tab2}})],1)],1)],1),t._v(" "),i("div",{staticClass:"box4-right"},[i("div",{staticClass:"box6-right"},[i("user-box")],1),t._v(" "),i("div",{staticClass:"box7-right"},[i("keep-alive",[i("pb-list",{attrs:{widgetMessage:t.tabs2[0],widgetType:1}})],1)],1),t._v(" "),i("div",{staticClass:"box12-right"},[t._m(2),t._v(" "),i("div",{staticStyle:{height:"270px",width:"100%"}},[i("keep-alive",[i("slide2")],1)],1)])])])},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("ul",{staticClass:"title"},[e("li",{staticClass:"list_title"},[e("a",{staticClass:"title-active",staticStyle:{"margin-left":"15px"}},[this._v("即将举办")])])])},function(){var t=this.$createElement,e=this._self._c||t;return e("ul",{staticClass:"title"},[e("li",{staticClass:"list_title"},[e("a",{staticClass:"title-active",staticStyle:{"margin-left":"15px"}},[this._v("往期回顾")])])])},function(){var t=this.$createElement,e=this._self._c||t;return e("ul",{staticClass:"title"},[e("li",{staticClass:"list_title"},[e("a",{staticClass:"title-active",staticStyle:{"margin-left":"15px"}},[this._v("广告招收栏目")])])])}]};var n=function(t){i("3wLc")},r=i("VU/8")(s.a,a,!1,n,"data-v-6e5f16f2",null);e.default=r.exports},vmSr:function(t,e,i){"use strict";(function(t){var s=i("G54G"),a=i("+GpL"),n=i("u8gt"),r=i("nDcq"),l=i("ISKb"),c=i("1h8J");e.a={name:"exhibition-default",data:function(){return{tabs:t.tabs,tab1:{message:{name:"即将举办",belong:"会议",requestPath:"/conference/getHoldingConferences",newsUrl:"/conference/getInfo"}},tab2:{message:{name:"往期会议",belong:"会议",requestPath:"/conference/getPassConference",newsUrl:"/conference/getInfo"}}}},filters:{FormatDate:function(t,e){return e&&(t=t.filter(function(t){if(t.name==e)return!0})),t}},computed:{tabs2:function(){var e,i,s=t.tabs;return e=s.filter(function(t){if("活动"==t.belong)return!0}),i=s.filter(function(t){if("精彩展会"==t.name)return!0}),s[0].message||s[0].message||s[0].message||c.i.call(this),s=[e,i]}},methods:{init:function(){var e=this.$router.options.routes[0].children.findIndex(function(t){return"展会"==t.showName});t.currentPath.data=[e]}},activated:function(){this.init()},components:{UserBox:s.a,slide2:a.a,PbList:r.a,currentLocate:n.a,mainListMeeting:l.a}}}).call(e,i("DuR2"))}});
//# sourceMappingURL=11.703891b5280dd5b8fa3a.js.map