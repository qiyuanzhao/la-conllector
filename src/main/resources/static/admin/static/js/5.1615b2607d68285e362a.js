webpackJsonp([5],{lrVb:function(e,t,l){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=l("zvf0"),r=l("mhoJ"),o=l("VU/8"),i=o(a.a,r.a,null,null,null);t.default=i.exports},mhoJ:function(e,t,l){"use strict";var a=function(){var e=this,t=e.$createElement,l=e._self._c||t;return l("div",{staticClass:"app-container"},[l("el-form",{ref:"form",attrs:{model:e.form,"label-width":"120px"}},[l("el-form-item",{attrs:{label:"Activity name"}},[l("el-input",{model:{value:e.form.name,callback:function(t){e.form.name=t},expression:"form.name"}})],1),e._v(" "),l("el-form-item",{attrs:{label:"Activity zone"}},[l("el-select",{attrs:{placeholder:"please select your zone"},model:{value:e.form.region,callback:function(t){e.form.region=t},expression:"form.region"}},[l("el-option",{attrs:{label:"Zone one",value:"shanghai"}}),e._v(" "),l("el-option",{attrs:{label:"Zone two",value:"beijing"}})],1)],1),e._v(" "),l("el-form-item",{attrs:{label:"Activity time"}},[l("el-col",{attrs:{span:11}},[l("el-date-picker",{staticStyle:{width:"100%"},attrs:{type:"date",placeholder:"Pick a date"},model:{value:e.form.date1,callback:function(t){e.form.date1=t},expression:"form.date1"}})],1),e._v(" "),l("el-col",{staticClass:"line",attrs:{span:2}},[e._v("-")]),e._v(" "),l("el-col",{attrs:{span:11}},[l("el-time-picker",{staticStyle:{width:"100%"},attrs:{type:"fixed-time",placeholder:"Pick a time"},model:{value:e.form.date2,callback:function(t){e.form.date2=t},expression:"form.date2"}})],1)],1),e._v(" "),l("el-form-item",{attrs:{label:"Instant delivery"}},[l("el-switch",{attrs:{"on-text":"","off-text":""},model:{value:e.form.delivery,callback:function(t){e.form.delivery=t},expression:"form.delivery"}})],1),e._v(" "),l("el-form-item",{attrs:{label:"Activity type"}},[l("el-checkbox-group",{model:{value:e.form.type,callback:function(t){e.form.type=t},expression:"form.type"}},[l("el-checkbox",{attrs:{label:"Online activities",name:"type"}}),e._v(" "),l("el-checkbox",{attrs:{label:"Promotion activities",name:"type"}}),e._v(" "),l("el-checkbox",{attrs:{label:"Offline activities",name:"type"}}),e._v(" "),l("el-checkbox",{attrs:{label:"Simple brand exposure",name:"type"}})],1)],1),e._v(" "),l("el-form-item",{attrs:{label:"Resources"}},[l("el-radio-group",{model:{value:e.form.resource,callback:function(t){e.form.resource=t},expression:"form.resource"}},[l("el-radio",{attrs:{label:"Sponsor"}}),e._v(" "),l("el-radio",{attrs:{label:"Venue"}})],1)],1),e._v(" "),l("el-form-item",{attrs:{label:"Activity form"}},[l("el-input",{attrs:{type:"textarea"},model:{value:e.form.desc,callback:function(t){e.form.desc=t},expression:"form.desc"}})],1),e._v(" "),l("el-form-item",[l("el-button",{attrs:{type:"primary"},on:{click:e.onSubmit}},[e._v("Create")]),e._v(" "),l("el-button",[e._v("Cancel")])],1)],1)],1)},r=[],o={render:a,staticRenderFns:r};t.a=o},zvf0:function(e,t,l){"use strict";t.a={data:function(){return{form:{name:"",region:"",date1:"",date2:"",delivery:!1,type:[],resource:"",desc:""}}},methods:{onSubmit:function(){this.$message("submit!")}}}}});
//# sourceMappingURL=5.1615b2607d68285e362a.js.map