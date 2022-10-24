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
                    label="1=草药代煎；2=核医学">
                        <a-input placeholder="请输入1=草药代煎；2=核医学" v-decorator="['tjRq', {rules: [{ required: true, message: '1=草药代煎；2=核医学不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="202210业务所属月份">
                        <a-input placeholder="请输入202210业务所属月份" v-decorator="['ny', {rules: [{ required: true, message: '202210业务所属月份不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="院区	2000(本院=2000;肿瘤=2100;西院=2200;金银湖=2300)">
                        <a-input placeholder="请输入院区	2000(本院=2000;肿瘤=2100;西院=2200;金银湖=2300)" v-decorator="['fyId', {rules: [{ required: true, message: '院区	2000(本院=2000;肿瘤=2100;西院=2200;金银湖=2300)不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="库位">
                        <a-input placeholder="请输入库位" v-decorator="['kw', {rules: [{ required: true, message: '库位不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="供应商ID">
                        <a-input placeholder="请输入供应商ID" v-decorator="['gysId', {rules: [{ required: true, message: '供应商ID不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="供应商名称">
                        <a-input placeholder="请输入供应商名称" v-decorator="['gysName', {rules: [{ required: true, message: '供应商名称不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="药品类别">
                        <a-input placeholder="请输入药品类别" v-decorator="['ypLb', {rules: [{ required: true, message: '药品类别不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="药品编码">
                        <a-input placeholder="请输入药品编码" v-decorator="['ypBh', {rules: [{ required: true, message: '药品编码不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="产地代码">
                        <a-input placeholder="请输入产地代码" v-decorator="['ypCd', {rules: [{ required: true, message: '产地代码不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="药品名称">
                        <a-input placeholder="请输入药品名称" v-decorator="['ypMc', {rules: [{ required: true, message: '药品名称不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="数量">
                        <a-input placeholder="请输入数量" v-decorator="['sl', {rules: [{ required: true, message: '数量不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="单位">
                        <a-input placeholder="请输入单位" v-decorator="['dw', {rules: [{ required: true, message: '单位不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="原进货价">
                        <a-input placeholder="请输入原进货价" v-decorator="['jhOld', {rules: [{ required: true, message: '原进货价不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="新进货价">
                        <a-input placeholder="请输入新进货价" v-decorator="['jhNew', {rules: [{ required: true, message: '新进货价不能为空' }] }]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="调价金额">
                        <a-input placeholder="请输入调价金额" v-decorator="['jhJe', {rules: [{ required: true, message: '调价金额不能为空' }] }]" />
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
        name: 'ScmWChangeAdd',
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
                scmWChange: {}
            }
        },
        methods: {
            reset () {
                this.loading = false
                this.scmWChange = {}
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
                    this.$post('scmWChange', {
                        ...this.scmWChange
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
                let values = this.form.getFieldsValue([                    'tjRq'    ,                     'ny'    ,                     'fyId'    ,                     'kw'    ,                     'gysId'    ,                     'gysName'    ,                     'ypLb'    ,                     'ypBh'    ,                     'ypCd'    ,                     'ypMc'    ,                     'sl'    ,                     'dw'    ,                     'jhOld'    ,                     'jhNew'    ,                     'jhJe'                   ])
                if (typeof values !== 'undefined') {
                    Object.keys(values).forEach(_key => { this.scmWChange[_key] = values[_key] })
                }
            }
        }
    }
</script>
