/**
* @description: Form表单数据处理组件
* @author liming.cen
* @date 2023年1月3日 上午11:33:00
*/
var formUtil = {
	/**
	* @description: Form表单数据自动获取并转换成json数据
	* @author liming.cen
	* @param {type} formIdName 表单Id名称（前缀不用加 # 号）
	*/
	formData2Json: function (formIdName) {
				//原始数据
				var originalFormData = $("#"+formIdName).serializeArray();
				//最终要返回的json数据
				var jsonData = {};
				//所有checkbox 名称 --> 数组 json映射
				var checkboxNamesArray = {};
				//所有 非checkbox 名称 
				var notCheckboxNames = [];
				//所有checkbox 名称 
				var checkboxNamesFromForm = [];
				//从serializeArray() 序列化数组中获取到的所有name
				var allNamesFromSerializeArray = [];
				//从serializeArray() 查找到的所有checkbox name
				var checkboxNamesFromSerializeArray = [];
				
				$("#"+formIdName+" input[type='checkbox']").each((i,item)=>{
					//帅选出表单中所有的 checkbox name
					if(checkboxNamesFromForm.indexOf(item.name)==-1){
						checkboxNamesFromForm.push(item.name);
					}
				});
			   
				originalFormData.forEach(item=>{
					//获取serializeArray()所有的 name
					if(allNamesFromSerializeArray.indexOf(item.name)==-1){
						allNamesFromSerializeArray.push(item.name);
					}
					//不是checkbox的item
					if(checkboxNamesFromForm.indexOf(item.name)==-1){
						notCheckboxNames.push(item.name);
					}else{
						//创建空的checkbox json 映射数据
						$(checkboxNamesArray).attr(item.name,new Array());
					}
				});
				//从serializeArray() 查找到的所有checkbox name
				allNamesFromSerializeArray.forEach(name=>{
					if(checkboxNamesFromForm.indexOf(name)!=-1){
						checkboxNamesFromSerializeArray.push(name);
					}
				});
				
				//遍历原始数据，拼装非 checkbox 数据和 checkbox 单独的json数据
				originalFormData.forEach(item=>{
					if(checkboxNamesFromForm.indexOf(item.name)==-1){
						$(jsonData).attr(item.name,item.value);
					}else{
						//筛选checkbox 数据，以 name--> array 的json 数据保存
						checkboxNamesArray[item.name].push(item.value);
					}
				});
				
				checkboxNamesFromForm.forEach(name=>{
					//将checkbox数据放到最终的json数据中
					$(jsonData).attr(name,checkboxNamesArray[name]);
				});
				
				//通过表单中的checkbox name 与 checkboxNamesFromSerializeArray 中的checkbox name 筛选出
				//serializeArray()中缺失的的checkbox，并进行json数据拼接。
				//因为 表单中的checkbox如果没有一个选中，那么serializeArray() 中将没有该checkbox的序列化数据
				checkboxNamesFromForm.forEach(name=>{
					if(checkboxNamesFromSerializeArray.indexOf(name)==-1){
						$(jsonData).attr(name,new Array());
					}
					
				});
				
				// console.log(notCheckboxNames);
				// console.log(checkboxNamesFromForm);
				// console.log(allNamesFromSerializeArray);
				// console.log(jsonData);
				// $( "input[type='checkbox'], input[type='radio']" ).click(function(){
				// 	formData2Json(formIdName);
				// });
				// $("select").change(function(){
				// 	formData2Json(formIdName);
				// });
				return jsonData;
				
			  }
};
