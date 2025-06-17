# Phân tích lỗ hổng CVE-2020-9483 trong Apache SkyWalking ≤ 8.3.0

**1. Thông tin chung**

CVE ID: CVE-2020-9483

Mức độ: High

Ảnh hưởng: Apache SkyWalking từ 6.x đến 8.3.0

Điều kiện: Ứng dụng sử dụng H2 Database (cấu hình mặc định)

**2. Mô tả lỗ hổng**

Lỗ hổng SQL Injection tồn tại trong lớp H2QueryDAO do thiếu kiểm tra và lọc đầu vào từ người dùng. Trường metricName được truyền từ truy vấn GraphQL tới hàm queryLogs(...), sau đó được chèn trực tiếp vào câu lệnh SQL mà không sử dụng cơ chế escape hoặc parameter binding.


**3. Chi tiết kỹ thuật**

•   API /graphql cho phép người dùng gửi truy vấn có chứa metricName

•   OAP nhận truy vấn này, truyền thẳng metricName vào queryLogs(...)

•   metricName được dùng trực tiếp trong sql.append(...) hoặc SelectQueryImpl.from(...)

•   Trong cấu hình mặc định của SkyWalking, H2 database được khởi động với quyền SA (Super Admin)

![image](https://github.com/user-attachments/assets/2f091608-c5ab-480f-8b6e-77e019d6ff93)
![image](https://github.com/user-attachments/assets/3490aff6-bb36-49b3-86e1-1703cd36998f)

Cơ sở dữ liệu h2 có chức năng ghi tệp FILE_WRITE (yêu cầu quyền quản trị để thực thi) và cả nội dung tệp và tên tệp đều có thể được kiểm soát.

![image](https://github.com/user-attachments/assets/ca477221-9f30-409a-a057-3d860106d5a3)
![image](https://github.com/user-attachments/assets/344c31ed-9d6b-426c-9d95-da5fad5a10ce)

Một hàm LINK_SCHEMA khác có thể chỉ định và khởi tạo yêu cầu jdbc hoặc jndi, nhưng vì không có sự phụ thuộc vào tomcat và springboot trong môi trường đích nên không thể sử dụng jndi chung 
trong phiên bản jdk cao hơn và cần phải tìm chuỗi tham chiếu trong sự phụ thuộc cục bộ.

![image](https://github.com/user-attachments/assets/1bdb9db1-2d5e-48d7-b744-a7796f096ffe)
![image](https://github.com/user-attachments/assets/8a4785d2-7f78-4194-aaa7-2f8a94eae342)

**4. Hậu quả**

Cho phép kẻ tấn công thực thi câu lệnh SQL nguy hiểm trên cơ sở dữ liệu.

Có thể ghi file tùy ý lên hệ thống (FILE_WRITE).

Trong một số trường hợp, kết hợp với các chức năng mở rộng có thể dẫn đến RCE.

Dữ liệu quan trọng có thể bị truy xuất, xóa, hoặc chỉnh sửa trái phép.

Demo:

Tạo payload trên msfvenom

![1](https://github.com/user-attachments/assets/d0ef027f-e6ba-43e9-b402-1e7da3010bd1)

Sử dụng metasploit để lắng nghe kết nối

![2](https://github.com/user-attachments/assets/8373721c-5144-4f09-add4-a27161dad04c)

Tạo file code.Java để đẩy payload shell_code.bin và cấp quyền chmod +x cho file vừa tải vào mục /tmp của server

![3](https://github.com/user-attachments/assets/c1f85579-ad6c-49fa-98fb-7d917160a96d)

Thực thi file code.java và chuyển về dạng hex

![4](https://github.com/user-attachments/assets/f62fcffe-e0df-4117-a578-6993a1922148)

Sử dung burpsuite để repeater /graphql

![5](https://github.com/user-attachments/assets/a28606f8-2013-4761-8c2e-3dae0905ff2b)

Sử dụng query bị dính lỗ hổng SQL injection để thực thi đoạn hex vừa tạo và đặt tên là code.class

![6](https://github.com/user-attachments/assets/3026590b-088d-4ac3-ad81-475a1108f8df)

Thực thi code và đẩy trực tiếp vào mục /tmp trên server

![7](https://github.com/user-attachments/assets/ebd55453-953e-4a63-9585-b547c4bc09c4)

Payload shell_code.bin đã được đưa vào và được cấp quyền chmod +x dựa trên filecode.java

![8](https://github.com/user-attachments/assets/f807fca4-ffe4-47a1-8755-b633101d31ff)

Chạy file run.java và chuyển về file hex tương tự như code.java để thực thi payload shell_code.bin

![9](https://github.com/user-attachments/assets/64fa13f0-3073-4fbf-97cc-1d88128298b5)

Đẩy tương tự như code.java và đặt tên là run.class đẩy qua server

![10](https://github.com/user-attachments/assets/6bbada02-77f2-4949-8b18-ec768980ceae)

Thực thi file run để chạy file run.class vừa tạo

![11](https://github.com/user-attachments/assets/f4b13fd0-ece0-4b39-a638-3c2e016bd25b)

Khai thác thành công

![12](https://github.com/user-attachments/assets/b91a0577-f659-4806-9ff3-1b65fa865319)

Demo chi tiết: https://drive.google.com/file/d/1eBvuMCrXEw2RjZpzHh01-9KJ2mooOFJj/view?usp=sharing
