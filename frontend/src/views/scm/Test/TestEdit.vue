<template>
    <a-drawer
            title="修改"
            :maskClosable="false"
            width=650
            placement="right"
            :closable="false"
            @close="onClose"
            :visible="editVisiable"
            style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
        <a-form :form="form">
                <a-form-item
                        v-bind="formItemLayout"
                        label="姓名">
                        <a-input placeholder="请输入姓名" v-decorator="['name', {rules: [{ required: true, message: '姓名不能为空' }] }]" />
                </a-form-item>
                <a-form-item
                        v-bind="formItemLayout"
                        label="年龄">
                        <a-input placeholder="请输入年龄" v-decorator="['age', {rules: [{ required: true, message: '年龄不能为空' }] }]" />
                </a-form-item>
                <a-form-item
                        v-bind="formItemLayout"
                        label="性别1是男2是女">
                        <a-input placeholder="请输入性别1是男2是女" v-decorator="['gener', {rules: [{ required: true, message: '性别1是男2是女不能为空' }] }]" />
                </a-form-item>
                <a-form-item
                        v-bind="formItemLayout"
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
    import moment from 'moment'

    const formItemLayout = {
        labelCol: { span: 3 },
        wrapperCol: { span: 18 }
    }
    export default {
        name: 'TestEdit',
        props: {
            editVisiable: {
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
                this.form.resetFields()
            },
            onClose () {
                this.reset()
                this.$emit('close')
            },
            setFormValues ({...test}) {
                let fields = [  'name'    , 'age'    , 'gener'    , 'birthday'         ]
                let fieldDates = [      'birthday'         ]
                Object.keys(test).forEach((key) => {
                    if (fields.indexOf(key) !== -1) {
                    this.form.getFieldDecorator(key)
                    let obj = {}
                    if(fieldDates.indexOf(key)!==-1)
                    {
                        if(test[key]!=='') {
                            obj[key] = moment(test[key])
                        }
                        else
                        {
                            obj[key] =''
                        }
                    } else {
                        obj[key] = test[key]
                    }
                    this.form.setFieldsValue(obj)
                }
            })
                this.test. id                     = test. id                                },
            handleSubmit () {
                this.form.validateFields((err, values) => {
                    if (!err) {
                    let test = this.form.getFieldsValue()
                    test. id                    = this.test. id                             this.$put('test', {
                        ...test
                    }).then(() => {
                        this.reset()
                        this.$emit('success')
                    }).catch(() => {
                        this.loading = false
                    })
                }
            })
            }
        }
    }
</script>
