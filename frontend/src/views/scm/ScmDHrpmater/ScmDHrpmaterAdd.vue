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
                    label="主键">
                        <a-input placeholder="请输入主键" v-decorator="['id', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="编码">
                        <a-input placeholder="请输入编码" v-decorator="['code', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="名字">
                        <a-input placeholder="请输入名字" v-decorator="['name', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="物料编码">
                        <a-input placeholder="请输入物料编码" v-decorator="['matnr', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="物料类型">
                        <a-input placeholder="请输入物料类型" v-decorator="['mtart', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="单位">
                        <a-input placeholder="请输入单位" v-decorator="['meins', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="单位描述">
                        <a-input placeholder="请输入单位描述" v-decorator="['mseht', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="物料描述">
                        <a-input placeholder="请输入物料描述" v-decorator="['maktx', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="拼音简码">
                        <a-input placeholder="请输入拼音简码" v-decorator="['zeinr', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="规格">
                        <a-input placeholder="请输入规格" v-decorator="['normt', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="状态">
                        <a-input placeholder="请输入状态" v-decorator="['state', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="药品类别">
                        <a-input placeholder="请输入药品类别" v-decorator="['bklas', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="是否删除">
                        <a-input placeholder="请输入是否删除" v-decorator="['isDeletemark', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="创建时间">
                        <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'createTime', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="修改时间">
                        <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'modifyTime', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="创建人">
                        <a-input placeholder="请输入创建人" v-decorator="['createUserId', {}]" />
                </a-form-item>
                <a-form-item v-bind="formItemLayout"
                    label="修改人">
                        <a-input placeholder="请输入修改人" v-decorator="['modifyUserId', {}]" />
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
        name: 'ScmDHrpmaterAdd',
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
                scmDHrpmater: {}
            }
        },
        methods: {
            reset () {
                this.loading = false
                this.scmDHrpmater = {}
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
                    this.$post('scmDHrpmater', {
                        ...this.scmDHrpmater
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
                let values = this.form.getFieldsValue([  'id'     ,  'code'     ,  'name'     ,  'matnr'     ,  'mtart'     ,  'meins'     ,  'mseht'     ,  'maktx'     ,  'zeinr'     ,  'normt'     ,  'state'     ,  'bklas'     ,  'isDeletemark'     ,  'createTime'     ,  'modifyTime'     ,  'createUserId'     ,  'modifyUserId'   ])
                if (typeof values !== 'undefined') {
                    Object.keys(values).forEach(_key => { this.scmDHrpmater[_key] = values[_key] })
                }
            }
        }
    }
</script>
