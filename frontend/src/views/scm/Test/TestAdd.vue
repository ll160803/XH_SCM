<template>
    <a-drawer
            title="新增"
            :maskClosable="false"
            width=650
            placement="right"
            :closable="false"
            @close="onClose"
            :visible="addVisiable"
            style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
        <a-form :form="form">
                <a-form-item v-bind="formItemLayout"
                    label="姓名">
                        <a-input placeholder="请输入姓名" v-decorator="['name', {rules: [{ required: true, message: '姓名不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="年龄">
                        <a-input placeholder="请输入年龄" v-decorator="['age', {rules: [{ required: true, message: '年龄不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="性别1是男2是女">
                        <a-input placeholder="请输入性别1是男2是女" v-decorator="['gener', {rules: [{ required: true, message: '性别1是男2是女不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="出生日期">
                        <a-date-picker v-decorator="[ 'birthday', {rules: [{ required: true, message: '出生日期不能为空' }] }]" />
                </a-form-item>
        </a-form>
        <div class="drawer-bootom-button">
            <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
                <a-button style="margin-right: .8rem">取消</a-button>
            </a-popconfirm>
            <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>
        </div>
    </a-drawer>
</template>
<script>
    const formItemLayout = {
        labelCol: { span: 3 },
        wrapperCol: { span: 18 }
    }
    export default {
        name: 'TestAdd',
        props: {
            addVisiable: {
                default: false
            }
        },
        data () {
            return {
                loading: false,
                formItemLayout,
                form: this.$form.createForm(this),
                test: {}
            }
        },
        methods: {
            reset () {
                this.loading = false
                this.test = {}
                this.form.resetFields()
            },
            onClose () {
                this.reset()
                this.$emit('close')
            },
            handleSubmit () {
                this.form.validateFields((err, values) => {
                    if (!err) {
                    this.setFields()
                    this.$post('test', {
                        ...this.test
                    }).then(() => {
                        this.reset()
                        this.$emit('success')
                    }).catch(() => {
                        this.loading = false
                    })
                }
            })
            },
            setFields () {
                let values = this.form.getFieldsValue([                    'name'    ,                     'age'    ,                     'gener'    ,                     'birthday'                   ])
                if (typeof values !== 'undefined') {
                    Object.keys(values).forEach(_key => { this.test[_key] = values[_key] })
                }
            }
        }
    }
</script>
