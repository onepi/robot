webpackJsonp([2],{"+PKg":function(t,e,a){"use strict";e.a={options1:[{id:"",name:"所有地区",label:"所有地区"}],options3:[{id:"",name:"所有行业",label:"所有行业"}],industryId:"",regionIds:[],filters:{name:""}}},"8TqU":function(t,e,a){"use strict";var n=a("mvHQ"),s=a.n(n),i=a("nfHz"),o=a("+PKg"),l=a("mw3O"),r=a.n(l),c={name:"job-table",data:function(){return{items:[],total:0,pageSize:15,currentPage:1,loading:!1,queryData:{content:o.a.filters.name.trim(),regionIds:o.a.regionIds,industryId:o.a.industryId}}},methods:{selectItem:function(t){var e=s()({id:t.id});this.$router.push({path:"/recruitment_1",query:{queryMsg:e}})},search:function(){var t=this,e=t.HOST+"/position/search",a=t.queryData;a.pageNum=t.currentPage;var n=r.a.stringify(a);t.loading=!0,t.axios({method:"post",data:n,url:e}).then(function(e){t.loading=!1;var a=e.data.message;t.total=a.total,t.pageSize=a.pageSize,t.items=a.list,a.list.forEach(function(e,a){t.items[a].createTime=e.createTime.slice(0,10)})}).catch(function(e){t.loading=!1,t.$message.error({showClose:!0,message:"获取招聘信息出现异常",duration:2e3})})},handleCurrentChange:function(t){this.currentPage=t,this.search(t,this.queryData)}},mounted:function(){var t=this;i.a.$on("jobSearch",function(){t.queryData={content:o.a.filters.name.trim(),regionIds:o.a.regionIds,industryId:o.a.industryId},t.search()})},activated:function(){this.search()}},u={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{attrs:{id:"job-table"}},[a("el-col",{directives:[{name:"loading",rawName:"v-loading",value:t.loading,expression:"loading"}],staticClass:"warp-main",attrs:{span:24,"element-loading-text":"拼命加载中"}},[a("el-table",{staticStyle:{width:"100%","margin-bottom":"2px"},attrs:{data:t.items,"highlight-current-row":""},on:{"row-click":t.selectItem}},[a("el-table-column",{attrs:{width:"10"}}),t._v(" "),a("el-table-column",{attrs:{type:"index",width:"40"}}),t._v(" "),a("el-table-column",{attrs:{type:"expand"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-form",{staticClass:"demo-table-expand",attrs:{"label-position":"left",inline:""}},[a("el-form-item",{attrs:{label:"[简介]"}},[a("div",{staticStyle:{background:"#e1e5f0"},domProps:{innerHTML:t._s(e.row.companyIntroduction)}})])],1)]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"positionName",label:"职位",width:"250",sortable:""}}),t._v(" "),a("el-table-column",{attrs:{prop:"companyName",label:"公司",width:"250",sortable:""}}),t._v(" "),a("el-table-column",{attrs:{prop:"degreeRequired",label:"职位要求",width:"150",sortable:""}}),t._v(" "),a("el-table-column",{attrs:{prop:"salary",label:"薪资",width:"150",sortable:""}}),t._v(" "),a("el-table-column",{attrs:{prop:"areas",label:"公司地点",width:"150",sortable:""}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime",label:"日期",width:"150",sortable:""}})],1),t._v(" "),a("el-col",{staticClass:"toolbar",attrs:{span:24}},[a("el-pagination",{staticStyle:{float:"right",margin:"10px"},attrs:{background:"",layout:"prev, pager, next",total:parseInt(t.total),"page-size":t.pageSize,"current-page":t.currentPage},on:{"current-change":t.handleCurrentChange,"update:currentPage":function(e){t.currentPage=e}}})],1)],1)],1)},staticRenderFns:[]};var d=a("VU/8")(c,u,!1,function(t){a("KNNj")},"data-v-da1c539a",null);e.a=d.exports},KNNj:function(t,e){},KTSZ:function(t,e,a){"use strict";(function(t){var n=a("u8gt"),s=a("ovKj"),i=a("8TqU");e.a={name:"recruitment-default",data:function(){return{queryData:{}}},computed:{},methods:{init:function(){var e=this.$router.options.routes[0].children.findIndex(function(t){return"招聘"==t.showName});t.currentPath.data=[e,0]}},activated:function(){this.init()},components:{currentLocate:n.a,JobSearch:s.a,JobTable:i.a}}}).call(e,a("DuR2"))},UJlU:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("KTSZ"),s={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",{attrs:{id:"recruitment-default"}},[e("div",{staticClass:"box1"},[e("currentLocate"),this._v(" "),e("div",{staticClass:"box2"},[this._m(0),this._v(" "),e("div",{staticStyle:{width:"100%"}},[e("keep-alive",[e("job-search")],1)],1)]),this._v(" "),e("div",{staticClass:"box1"},[e("keep-alive",[e("job-table")],1)],1)],1)])},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("ul",{staticClass:"title"},[e("li",{staticClass:"list_title"},[e("a",{staticClass:"active",staticStyle:{"margin-left":"15px"}},[this._v("职位搜索")])])])}]};var i=function(t){a("xglB")},o=a("VU/8")(n.a,s,!1,i,"data-v-a390f616",null);e.default=o.exports},bPAs:function(t,e,a){"use strict";(function(t){var n=a("+PKg"),s=a("nfHz");e.a={naem:"job-search",data:function(){return{filters:n.a.filters,options1:n.a.options1,options2:[{label:"热门城市",options:[]},{label:"城市名",options:[{id:0,name:"所有城市",label:"所有城市"}]}],options3:n.a.options3,options4:[],options5:[],value1:"",value2:"",value3:"",value4:"",value5:"",isBan:[!1,!1,!1],industryId:n.a.industryId,regionIds:n.a.regionIds,url:["/position/getLevelAreas","/position/getLevelIndustry","/position/search"]}},methods:{init:function(){var t=this,e=t.HOST+t.url[0];t.axios.get(e).then(function(e){n.a.options1=[{id:"",name:"所有地区",label:"所有地区"}],Array.prototype.push.apply(n.a.options1,e.data.message),t.options1.push.apply(t.options1,e.data.message)}).catch(function(e){t.$message.error({showClose:!0,message:"搜索地区数据异常",duration:2e3})}),e=t.HOST+t.url[1],t.axios.get(e).then(function(e){n.a.options3=[{id:"",name:"所有行业",label:"所有行业"}],Array.prototype.push.apply(n.a.options3,e.data.message),t.options3.push.apply(t.options3,e.data.message)}).catch(function(e){t.$message.error({showClose:!0,message:"搜索行业数据异常",duration:2e3})})},changeArea:function(t){var e=this;if(""!=t){var a=e.HOST+e.url[0],n={parentId:t};e.axios.get(a,{params:n}).then(function(t){""!=t.data.message?(e.options2[1].options=[],e.options2[1].options.push.apply(e.options2[1].options,t.data.message),e.$set(e.isBan,e.isBan[0],e.isBan[0]=!1)):(e.options2=[{label:"热门城市",options:[]},{label:"城市名",options:[{id:0,name:"所有城市",label:"所有城市"}]}],e.$set(e.isBan,e.isBan[0],e.isBan[0]=!0),e.value2=[])}).catch(function(t){e.$message.error({showClose:!0,message:"请求出现异常",duration:2e3})})}},changeIndustry1:function(t){var e=this;if(""!=t){var a=e.HOST+e.url[1],n={parentId:t};e.axios.get(a,{params:n}).then(function(t){""!=t.data.message?(e.options4=t.data.message,e.$set(e.isBan,e.isBan[1],e.isBan[1]=!1)):(e.options4=[],e.$set(e.isBan,e.isBan[1],e.isBan[1]=!0),e.$set(e.isBan,e.isBan[2],e.isBan[2]=!0),e.value4=e.value5=[])}).catch(function(t){e.$message.error({showClose:!0,message:"请求出现异常",duration:2e3})})}},changeIndustry2:function(t){var e=this;if(""!=t){var a=e.HOST+e.url[1],n={parentId:t};e.axios.get(a,{params:n}).then(function(t){""!=t.data.message?(e.options5=t.data.message,e.$set(e.isBan,e.isBan[2],e.isBan[2]=!1)):(e.options5=[],e.$set(e.isBan,e.isBan[2],e.isBan[2]=!0))}).catch(function(t){e.$message.error({showClose:!0,message:"请求出现异常",duration:2e3})})}},AreaChoose:function(t){this.regionIds=t},industryChoose:function(t){this.industryId=t},searching:function(){n.a.industryId=this.industryId,n.a.regionIds=this.regionIds,n.a.filters=this.filters,console.log(t.currentPath.data),8==t.currentPath.data[0]?this.$router.push({path:"/recruitment"}):(console.log("eventBus"),s.a.$emit("jobSearch"))}},activated:function(){1==this.options1.length&&1==this.options3.length&&this.init()}}}).call(e,a("DuR2"))},ovKj:function(t,e,a){"use strict";var n=a("bPAs"),s={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{attrs:{id:"job-search"}},[a("div",{staticClass:"menus"},[a("div",{staticClass:"row row-first"},[t._m(0),t._v(" "),a("div",{staticClass:"body"},[a("el-select",{attrs:{filterable:"",placeholder:"选择省份/直辖市/自治区"},on:{change:t.changeArea},model:{value:t.value1,callback:function(e){t.value1=e},expression:"value1"}},t._l(t.options1,function(e,n){return a("el-option",{key:n,attrs:{label:e.name,value:e.id}},[a("span",{staticStyle:{"text-align":"center",color:"#8492a6","font-size":"13px"}},[t._v(t._s(e.name))])])})),t._v(" "),a("el-select",{attrs:{disabled:t.isBan[0],placeholder:"选择城市",multiple:""},on:{change:t.AreaChoose},model:{value:t.value2,callback:function(e){t.value2=e},expression:"value2"}},t._l(t.options2,function(e){return a("el-option-group",{key:e.label,attrs:{label:e.label}},t._l(e.options,function(t){return a("el-option",{key:t.name,attrs:{label:t.name,value:t.id}})}))}))],1)]),t._v(" "),a("div",{staticClass:"row"},[t._m(1),t._v(" "),a("div",{staticClass:"body"},[a("el-select",{attrs:{filterable:"",placeholder:"行业类型"},on:{change:t.changeIndustry1},model:{value:t.value3,callback:function(e){t.value3=e},expression:"value3"}},t._l(t.options3,function(e,n){return a("el-option",{key:n,attrs:{label:e.name,value:e.id}},[a("span",{staticStyle:{color:"#8492a6","font-size":"13px"}},[t._v(t._s(e.name))])])})),t._v(" "),a("el-select",{attrs:{disabled:t.isBan[1],filterable:"",placeholder:"行业"},on:{change:t.changeIndustry2},model:{value:t.value4,callback:function(e){t.value4=e},expression:"value4"}},t._l(t.options4,function(e,n){return a("el-option",{key:n,attrs:{label:e.name,value:e.id}},[a("span",{staticStyle:{color:"#8492a6","font-size":"13px"}},[t._v(t._s(e.name))])])})),t._v(" "),a("el-select",{attrs:{disabled:t.isBan[2],filterable:"",placeholder:"职位"},on:{change:t.industryChoose},model:{value:t.value5,callback:function(e){t.value5=e},expression:"value5"}},t._l(t.options5,function(e){return a("el-option",{key:e.name,attrs:{label:e.name,value:e.id}},[a("span",{staticStyle:{color:"#8492a6","font-size":"13px"}},[t._v(t._s(e.name))])])}))],1)]),t._v(" "),a("el-form",{attrs:{inline:!0,model:t.filters}},[a("el-form-item",[a("el-input",{staticStyle:{"min-width":"240px",width:"400px"},attrs:{clearable:"",placeholder:"默认搜索显示招聘列表"},model:{value:t.filters.name,callback:function(e){t.$set(t.filters,"name",e)},expression:"filters.name"}})],1),t._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",icon:"el-icon-search"},on:{click:function(e){t.throttle(t.searching,this)}}},[t._v("搜索")])],1)],1)],1)])},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"head"},[e("span",{staticClass:"title",attrs:{title:"地区"}},[this._v("地区：")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"head"},[e("span",{staticClass:"title",attrs:{title:"行业/职位："}},[this._v("行业/职位：")])])}]};var i=function(t){a("qI80")},o=a("VU/8")(n.a,s,!1,i,"data-v-7f442c9e",null);e.a=o.exports},qI80:function(t,e){},xglB:function(t,e){}});
//# sourceMappingURL=2.13d5cad9221174667e37.js.map