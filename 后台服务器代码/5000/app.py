from flask import Flask, Response, request, render_template
from werkzeug.utils import secure_filename
from enlsb import func
import os

app = Flask(__name__)

# 设置图片保存文件夹
UPLOAD_FOLDER = '//photo'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# 设置允许上传的文件格式
ALLOW_EXTENSIONS = ['png', 'PNG']


# 判断文件后缀是否在列表中
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOW_EXTENSIONS


# 上传图片
@app.route("/photo/upload", methods=['POST', "GET"])
def uploads():
    if request.method == 'POST':
        # 获取post过来的文件名称，从name=file参数中获取
        basepath = os.path.dirname(__file__)

        f = request.form.get('file1')
        file_handle = open(os.path.join(basepath,'photo','text.txt'),mode='w')
        file_handle.write(f)
        file_handle.close()
        file = request.files['file']

        res = {}

        if file and allowed_file(file.filename) :
            print(file.filename)
            # secure_filename方法会去掉文件名中的中文
            basepath = os.path.dirname(__file__)
            upload_path1 = os.path.join(basepath,'photo','text.txt')
            upload_path2 = os.path.join(basepath,'photo',secure_filename(file.filename))
            # 保存图片
            
            file.save(upload_path2)
            func(upload_path2, upload_path1, os.path.join(basepath,'photo',"1.png"))

            res['code'] = 0
            res['msg'] = 'success'

            return render_template('ok.html')
        else:
            res['code']=1
            res['msg'] = 'fail'
            return "格式错误，请上传png和txt格式文件"
    return render_template('index.html')


# 查看图片
@app.route("/photo/<imageId>.png")
def get_frame(imageId):
    # 图片上传保存的路径//D:\LSBPY\photo
    with open(r'/home/LSBPY/photo/{}.png'.format(imageId), 'rb') as f:
        # 字符串化，使用utf-8的方式解析二进制
        image = f.read()
        resp = Response(image, mimetype="image/png")
        return resp




if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)
