create database supermarket
use supermarket 
go

--一.
--建表
--1.建立供应商表
create table Suppliers(
Supplier_ID nchar(5) primary key,
Supplier_Name nvarchar(20),
Supplier_Address nvarchar(30),
Supplier_Phone nchar(11))

select * from Suppliers
select * from Customer
select *from Employee

--加约束
alter table Suppliers add constraint Suppliers_Phone_c check(Supplier_Phone like '[1][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]')

--2.建立仓库表
create table Store(
Store_ID nchar(4) primary key,
Store_Area int,
Store_Address nvarchar(30),
Store_Maxstock int)
select * from Store
--加约束

--3.建立商品表
create table Goods(
Goods_ID nchar(4) primary key,
Goods_Name nvarchar(30) not null,
Goods_UnitPrice money not null,
Goods_Type nvarchar(5),
Store_ID nchar(4),
Goods_Qualitydate date not null,
Goods_Stock int,
Goods_Image VARBINARY(MAX),
Goods_Info VARCHAR(MAX),
Goods_Discount numeric(2,1),
Goods_Condition nchar(4),
Goods_Show nchar(6),
Goods_Outdatewarn nvarchar(8),
Goods_Storewarn nvarchar(8))
--IMAGE 数据类型与 TEXT 数据类型在早期版本的 SQL Server 中被广泛使用，但从 SQL Server 2005 开始，Microsoft 将其标记为过时的。

--加约束
alter table Goods add constraint Goods_StoreID_f foreign key(Store_ID) references Store(Store_ID)
alter table Goods add constraint Goods_Qualitydate_d default(DateAdd(mm,6,getDate())) for Goods_Qualitydate
alter table Goods add constraint Goods_Type_c check(Goods_Type in('日用品','食品','家电','数码产品','服装','文具','玩具','其他'))
alter table Goods add constraint Goods_Type_d default('其他') for Goods_Type
alter table Goods add constraint Goods_Discount_d default(1.0) for Goods_Discount
alter table Goods add constraint Goods_Discount_c check(Goods_Discount between 0.0 and 1.0)
alter table Goods add constraint Goods_Condition_c check(Goods_Condition in('在售','预售'))
alter table Goods add constraint Goods_Condition_d default('在售') for Goods_Condition
alter table Goods add constraint Goods_Name_u unique(Goods_Name)
alter table Goods add constraint Goods_Show_d default('A00') for Goods_Show
alter table Goods add constraint Goods_Outdatewarn_c check(Goods_outdatewarn in('未过期','过期警告'))
alter table Goods add constraint Goods_Outdatewarn_d default('未过期') for Goods_outdatewarn
alter table Goods add constraint Goods_Storewarn_c check(Goods_Storewarn in('库存正常','库存警告'))
alter table Goods add constraint Goods_Storewarn_d default('库存正常') for Goods_Storewarn
alter table Goods alter column Goods_Info text
alter table Goods alter column Goods_Info nvarchar(255)
alter table Goods alter column Goods_Image nvarchar(255)

select * from Goods

--4.建立职务表
create table Job(
Job_ID nchar(1) primary key,
Job_Name nvarchar(30) not null,
Job_MiniSalary money,
Job_MaxSalary money,
Job_Info VARCHAR(MAX),
Job_Permissions VARCHAR(MAX))
select *from Job
--加约束
alter table Job add constraint Job_Name_u unique(Job_Name)
alter table Job add constraint Job_MiniSalary_d default(2000) for Job_MiniSalary
alter table Job add constraint Job_MiniSalary_c check(Job_MiniSalary>=2000)
update Job set Job_Name='收银员' where Job_ID='B'
update Job set Job_Name='仓库管理员' where Job_ID='C'
update Job set Job_Name='商品管理员' where Job_ID='D'

--5.建立员工表
create table Employee(
Employee_ID nchar(5) primary key,
Employee_Name nvarchar(20) not null,
Employee_Salary money,
Job_ID nchar(1),
Employee_Phone nchar(11),
Employee_Epassword nvarchar(20),
Employee_Age int)

select * from Employee
--加约束
alter table Employee add constraint Employee_JobID_f foreign key(Job_ID) references Job(Job_ID)
alter table Employee add constraint Employee_Phone_c check(Employee_Phone like '[1][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]')
--保证登陆密码存在字母且长度>=6位
alter table Employee add constraint Employee_Epassword_c check(Employee_Epassword like '[A-Z,a-z]_____%')
alter table Employee add constraint Employee_Age_c check(Employee_Age>=18)
alter table Employee add constraint Employee_JobID_d default('00') for Job_ID
alter table Employee add constraint Employee_Epassword_d default('00') for Employee_Epassword

--6.建立会员等级表
create table VIP(
VIP_ID nchar(1) primary key,
VIP_Name nchar(8),
VIP_Condition VARCHAR(MAX) not null,
VIP_Discount numeric(2,1))
select *from VIP 
--加约束
alter table VIP add constraint VIP_Discount_c check (VIP_Discount between 0.0 and 1.0)
alter table VIP add constraint VIP_Discount_d default(1.0) for VIP_Discount

--7.建立客户表
create table Customer(
Customer_ID nchar(5) primary key,
Customer_Password nvarchar(20),
Customer_Name nvarchar(20) not null,
VIP_ID nchar(1),
Customer_Deposit money,
Customer_Phone nchar(11),
Customer_Birthday date)

--加约束
--保证登陆密码存在字母且长度>=6位
alter table Customer add constraint Customer_Password_c check(Customer_Password like '[A-Z,a-z]_____%')
alter table Customer add constraint Customer_VIPID_f foreign key(VIP_ID) references VIP(VIP_ID)
alter table Customer add constraint Customer_Phone_c check(Customer_Phone like '[1][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]')
alter table Customer add constraint Customer_Deposit_d default(0) for Customer_Deposit
alter table Customer add constraint Customer_VIPID_d default('C') for VIP_ID
select *from Customer 
--8.建立销售单表
create table Sell(
Sell_ID nchar(5) primary key,
Customer_ID nchar(5),
Employee_ID nchar(5),
Sell_TotalMoney money,
Sell_Date date,
Sell_FinishDate date,
Sell_OrderType nvarchar(6))

--加约束
alter table Sell add constraint Sell_CustomerID_f foreign key(Customer_ID) references Customer(Customer_ID)
alter table Sell add constraint Sell_EmployeeID_f foreign key(Employee_ID) references Employee(Employee_ID)
alter table Sell add constraint Sell_Date_d default(getdate()) for Sell_Date
alter table Sell add constraint Sell_FinishDate_d default(getdate()) for Sell_FinishDate
alter table Sell add constraint Sell_OrderType_c check (Sell_OrderType in('预订单','现单'))
alter table Sell add constraint Sell_OrderType_d default('现单') for Sell_OrderType

select *from sell
select *from Return_
select *from Financial
delete from Return_

--9.建立销售细节表
create table SellInfo(
Goods_ID nchar(4),
Sell_ID nchar(5),
SellInfo_Quantity int,
SellInfo_UnitPrice money,
SellInfo_Discount numeric(2,1),
SellInfo_Total money,
Goods_Type nvarchar(5),
constraint SellInfo_p primary key(Goods_ID,Sell_ID))

--加约束
alter table SellInfo add constraint SellInfo_GoodsID_f foreign key(Goods_ID) references Goods(Goods_ID)
alter table SellInfo add constraint SellInfo_Sell_ID_f foreign key(Sell_ID) references Sell(Sell_ID)
alter table SellInfo add constraint SellInfo_Discount_d default(1.0) for SellInfo_Discount
alter table SellInfo add constraint SellInfo_Discount_c check(SellInfo_Discount between 0.0 and 1.0)

--10.建立退货订单表
create table Return_(
Sell_ID nchar(5),
Employee_ID nchar(5),
Return_Date date,
Return_Charges money,
Return_Remark VARCHAR(MAX) not null,
constraint Return_p primary key(Sell_ID))

--加约束
alter table Return_ add constraint Return_SellID_f foreign key(Sell_ID) references Sell(Sell_ID)
alter table Return_ add constraint Return_Employee_ID foreign key(Employee_ID) references Employee(Employee_ID)
alter table Return_ add constraint Return_Date_d default(getdate()) for Return_Date
alter table Return_ drop constraint Return_SellID_f

--11.建立进货单表
create table Orders(
Orders_ID nchar(5),
Supplier_ID nchar(5),
Orders_ShippedDate date,
Orders_ArriveDate date,
Orders_Condition nvarchar(4),
Orders_FinishDate date,
Orders_Money money,
constraint Orders_p primary key(Orders_ID))

select * from Orders
select * from Order_Details
select * from Suppliers
select * from Goods
select * from Financial
--加约束
alter table Orders add constraint Orders_SupplierID_f foreign key(Supplier_ID) references Suppliers(Supplier_ID)
alter table Orders add constraint Orders_ShippedDate_d default(getdate()) for Orders_ShippedDate
alter table Orders add constraint Orders_ArriveDate_d default(DateAdd(mm,1,getDate())) for Orders_ArriveDate
alter table Orders add constraint Orders_Condition_c check(Orders_Condition in('运输','入库','退货'))
alter table Orders add constraint Orders_Condition_d default('运输') for Orders_Condition
alter table Orders add constraint Orders_Money_d default(0) for Orders_Money
alter table Orders alter column Orders_Money float
alter table Orders drop constraint Orders_Money_d 

--12.建立进货细节表
create table Order_Details(
Orders_ID nchar(5),
Goods_ID nchar(4),
Order_Details_Quantity int,
Order_Details_UnitPrice money,
Order_Details_Discount numeric(2,1),
Order_Details_Price money,
Goods_Type nvarchar(5),
constraint Order_Details_p primary key(Orders_ID,Goods_ID))

--加约束
alter table Order_Details add constraint Order_Details_OrdersID_f foreign key(Orders_ID) references Orders(Orders_ID)
alter table Order_Details add constraint Order_Details_GoodsID_f foreign key(Goods_ID) references Goods(Goods_ID)
alter table Order_Details add constraint Order_Details_Discount_d default(1.0) for Order_Details_Discount
alter table Order_Details add constraint Order_Details_Discount_c check(Order_Details_Discount between 0.0 and 1.0)
alter table Order_Details alter column Order_Details_UnitPrice float
alter table Order_Details drop constraint Orders_Money_d 

--13.建立财务表
create table Financial(
Orders_ID nchar(5),
Sell_ID nchar(5),
Goods_ID nchar(4),
Financial_Type nvarchar(4),
Financial_Amount money,
Financial_Date date
)

--加约束
alter table Financial add constraint Financial_Orders_ID_f foreign key(Orders_ID) references Orders(Orders_ID)
alter table Financial add constraint Financial_Sell_ID_f foreign key(Sell_ID) references Sell(Sell_ID)
alter table Financial add constraint Financial_Goods_ID_f foreign key(Goods_ID) references Goods(Goods_ID)
alter table Financial add constraint Financial_Type_c check(Financial_Type in('进货','退货','销售','退款'))
alter table Financial add constraint Financial_Date_d default(getdate()) for Financial_Date
alter table Financial drop constraint Financial_Orders_ID_f
alter table Financial drop constraint Financial_Sell_ID_f

--建视图
-- 视图管理
-- 建立权限信息查询视图
--1、商品基本信息视图，只有基本信息没有敏感信息
go
create view goods_view
as
select GoodsID,GoodsName,UnitPrice,Discount,GoodsClassID,Qualitydate,Producedate,Goodsinfo
from Goods
go

--2、收银员工信息视图
go
create view employee_shouyin_view
as
select *
from Employee
where Epower='收银'

--3、仓库员工信息视图
go
create view employee_cangku_view
as
select *
from Employee
where Epower='监管仓库'

--4、商品管理员工信息视图
go
create view employee_goods_view
as
select *
from Employee
where Epower='管理商品'

--5、 商品高级信息视图
go
create view goodsgaoji_view
as
select GoodsID,GoodsName,StoreID,Storenum,SuppilerID,outdatewarn,storewarn,storestandardnum
from Goods
go
