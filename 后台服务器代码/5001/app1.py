
import demjson as demjson
from flask import Flask, Response, request, render_template
from werkzeug.utils import secure_filename
from delsb import func1
import os

app = Flask(__name__)

# 设置图片保存文件夹
UPLOAD_FOLDER = 'photo'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# 设置允许上传的文件格式
ALLOW_EXTENSIONS = ['png', 'txt']


# 判断文件后缀是否在列表中
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOW_EXTENSIONS


# 上传图片
@app.route("/photo/upload", methods=['POST', "GET"])
def uploads():
    if request.method == 'POST':
        # 获取post过来的文件名称，从name=file参数中获取

        file = request.files['file']
        res = {}

        if file and allowed_file(file.filename) :
            print(file.filename)
            # secure_filename方法会去掉文件名中的中文
            basepath = os.path.dirname(__file__)
            file_name = secure_filename(file.filename)
            upload_path1 = os.path.join(basepath,'photo',file_name)
            upload_path2 = os.path.join(basepath,'photo','aa.txt')
            # 保存图片
            file.save(upload_path1)
            func1(30, upload_path1, upload_path2)

            res['code'] = 0
            res['msg'] = 'success'

            return render_template('ok.html')
        else:
            res['code']=1
            res['msg'] = 'fail'
            return "格式错误，请上传png格式文件"
    return render_template('index1.html')




@app.route("/photo/<txtid>.txt")
def get_frame1(txtid):
    with open(r'/home/LSBPY/photo/{}.txt'.format(txtid), 'r')as f:
        txt = f.readlines()
        json = demjson.encode(txt)
        res = {}
        res['code'] = 0
        res['data'] = txt
        print(res)
        return res



if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001, debug=True)
