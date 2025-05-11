// 使用DOMPurify库进行XSS防御的核心代码

/*全局事件监听 */
// 覆盖'input'(输入时)、'paste'(粘贴时)、'change'(值改变时)三种用户输入场景
['input', 'paste', 'change'].forEach(eventName => {
    // 在document上添加事件监听器（第三个参数true表示在捕获阶段处理）
    document.addEventListener(eventName, function(event) {
        // 获取事件触发的目标元素
        const target = event.target;

        // 仅处理<input>和<textarea>元素
        if (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA') {
            // 使用DOMPurify进行XSS过滤配置
            // ALLOWED_TAGS: [] 表示删除所有HTML标签，保留纯文本内容
            const cleanValue = DOMPurify.sanitize(target.value, { ALLOWED_TAGS: [] });
            console.log("净化后的值："+cleanValue);
            console.log("初始输入值："+target.value);
            // 检测经过DOMPurify转换后的值是否与原始值不同
            if (cleanValue !== target.value) {
                target.value = cleanValue;
                // 使用layer弹层库显示警告信息（需确保已加载该库）
                layer.msg('非法字符已被替换，请重新输入！', {
                    icon: 5,            
                    time: 2000,         
                    anim: 6,            // 使用抖动动画类型
                    offset: [           
                        $(window).height() - 480,  
                        $(window).width() - 890    
                    ]
                });
            }
        }
    }, true); // 使用捕获阶段确保优先处理
});

/* 覆盖原生value属性。
 * 作用：封堵程序化注入的漏洞。
 * 比如通过程序 input.value = '<script>...'直接赋值时，浏览器事件（input/change）监听失效，出现XSS漏洞
 */
// 创建通用净化函数（移除所有HTML标签）
const sanitizeValue = (newValue) => DOMPurify.sanitize(newValue, { ALLOWED_TAGS: [] });

/* 重写HTMLInputElement的value属性setter */
// 获取原生value属性描述符
const nativeInputSetter = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value').set;
// 定义新的value属性setter
Object.defineProperty(HTMLInputElement.prototype, 'value', {
    set: function(value) {
        // 调用原生setter方法，传入净化后的值
        nativeInputSetter.call(this, sanitizeValue(value));
    }
});

/* 重写HTMLTextAreaElement的value属性setter */
// 获取原生value属性描述符
const nativeTextAreaSetter = Object.getOwnPropertyDescriptor(HTMLTextAreaElement.prototype, 'value').set;
// 定义新的value属性setter
Object.defineProperty(HTMLTextAreaElement.prototype, 'value', {
    set: function(value) {
        // 调用原生setter方法，传入净化后的值
        nativeTextAreaSetter.call(this, sanitizeValue(value));
    }
});