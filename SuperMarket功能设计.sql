use supermarket
go

--二.
--功能实现
--（一）基本功能实现
--1.基础信息管理
--1.1客户（会员）信息管理（添加、修改与删除）
--添加会员
create proc Add_Customer @Customer_ID nchar(5),@Customer_Password nvarchar(20),@Customer_Name nvarchar(20),@Customer_Phone nchar(11),@Customer_Birthday date
as
insert into Customer(Customer_ID,Customer_Password,Customer_Name,Customer_Phone,Customer_Birthday)
values(@Customer_ID,@Customer_Password,@Customer_Name,@Customer_Phone,@Customer_Birthday)

--会员密码修改
create proc Update_Customer_Password @Customer_ID nchar(5),@Customer_Password nvarchar(20)
as
update Customer
set Customer_Password=@Customer_Password
where Customer_ID=@Customer_ID

--会员积分修改
create proc Update_Customer_Deposit @Customer_ID nchar(5),@add money
as
update Customer 
set Customer_Deposit=Customer_Deposit+@add
where Customer_ID=@Customer_ID

--会员基本信息修改
create proc Update_Customer_Data @Customer_ID nchar(5),@Customer_Name nvarchar(20),@Customer_Phone nchar(11),@Customer_Birthday date
as
update Customer
set Customer_Name=@Customer_Name,Customer_Phone=@Customer_Phone,Customer_Birthday=@Customer_Birthday
where Customer_ID=@Customer_ID

--删除会员
create proc Delete_Customer @Customer_ID nchar(5)
as 
delete
from Customer
where Customer_ID=@Customer_ID

--1.2商品信息管理（添加、修改与删除）
--添加商品
create proc Add_Goods @Goods_ID nchar(4),@Goods_Name nvarchar(30),@Goods_UnitPrice money,@Goods_Type nvarchar(5),@StoreID nchar(10),@Goods_Qualitydate date,@Goods_Stock int,@Goods_Image VARBINARY(MAX),@Goods_Info VARCHAR(MAX),@Goods_Discount numeric(2,1),@Goods_Condition nchar(4),@Goods_Show nchar(6),@Goods_Outdatewarn nvarchar(8),@Goods_Storewarn nvarchar(8)
as
insert into Goods(Goods_ID,Goods_Name,Goods_UnitPrice,Goods_Type,Store_ID,Goods_Qualitydate,Goods_Stock,Goods_Image,Goods_Info,Goods_Discount,Goods_Condition,Goods_Show,Goods_Outdatewarn,Goods_Storewarn)
values(@Goods_ID,@Goods_Name,@Goods_UnitPrice,@Goods_Type,@StoreID,@Goods_Qualitydate,@Goods_Stock,@Goods_Image,@Goods_Info,@Goods_Discount,@Goods_Condition,@Goods_Show,@Goods_Outdatewarn,@Goods_Storewarn)

--商品基本信息修改
create proc Update_Goods_Data @Goods_ID nchar(4),@Goods_Name nvarchar(30),@Goods_UnitPrice money,@Goods_Type nvarchar(5),@StoreID nchar(10),@Goods_Qualitydate date,@Goods_Stock int,@Goods_Image VARBINARY(MAX),@Goods_Info VARCHAR(MAX),@Goods_Discount numeric(2,1),@Goods_Condition nchar(4),@Goods_Show nchar(6),@Goods_Outdatewarn nvarchar(8),@Goods_Storewarn nvarchar(8)
as
update Goods
set Goods_Name=@Goods_Name,Goods_UnitPrice=@Goods_UnitPrice,Goods_Type=@Goods_Type,Store_ID=@StoreID,Goods_Qualitydate=@Goods_Qualitydate,Goods_Stock=@Goods_Stock,Goods_Image=@Goods_Image,Goods_Info=@Goods_Info,Goods_Discount=@Goods_Discount,Goods_Condition=@Goods_Condition,Goods_Show=@Goods_Show,Goods_Outdatewarn=@Goods_Outdatewarn,Goods_Storewarn=@Goods_Storewarn
where Goods_ID=@Goods_ID

--删除商品
create proc Delete_Goods @Goods_ID nchar(4)
as 
delete
from Goods
where Goods_ID=@Goods_ID


--1.3供应商信息管理（添加、修改与删除）
--添加供应商
create proc Add_Suppliers @Supplier_ID nchar(5),@Supplier_Name nvarchar(20),@Supplier_Address nvarchar(30),@Supplier_Phone nchar(11)
as
insert into Suppliers(Supplier_ID,Supplier_Name,Supplier_Address,Supplier_Phone )
values(@Supplier_ID,@Supplier_Name,@Supplier_Address,@Supplier_Phone )

--供应商基本信息修改
create proc Update_Suppliers_Data @Supplier_ID nchar(5),@Supplier_Name nvarchar(20),@Supplier_Address nvarchar(30),@Supplier_Phone nchar(11)
as
update Suppliers
set Supplier_Name=@Supplier_Name,Supplier_Address=@Supplier_Address,Supplier_Phone=@Supplier_Phone
where Supplier_ID=@Supplier_ID

--删除供应商
create proc Delete_Suppliers @Supplier_ID nchar(5)
as 
delete
from Suppliers
where Supplier_ID=@Supplier_ID


--2.进货管理
--2.1进货管理
create proc Manage_Order @Orders_ID nchar(5),@Supplier_ID nchar(5),@Orders_ShippedDate date,@Orders_ArriveDate date,@Orders_Condition nvarchar(4),@Orders_FinishDate date
as
insert into Orders(Orders_ID,Supplier_ID,Orders_ShippedDate,Orders_ArriveDate,Orders_Condition,Orders_FinishDate)
values(@Orders_ID,@Supplier_ID,@Orders_ShippedDate,@Orders_ArriveDate,@Orders_Condition,@Orders_FinishDate)

--2.2进货细节管理
create proc Manage_Order_Details @Orders_ID nchar(5),@Goods_ID nchar(4),@Order_Details_Quantity int,@Order_Details_UnitPrice money,@Order_Details_Discount numeric(2,1),@Order_Details_Price money
as
insert into Order_Details(Orders_ID,Goods_ID,Order_Details_Quantity,Order_Details_UnitPrice,Order_Details_Discount,Order_Details_Price)
values(@Orders_ID,@Goods_ID,@Order_Details_Quantity,@Order_Details_UnitPrice,@Order_Details_Discount,@Order_Details_Price)

create trigger tri_Orders_Money on Order_Details for insert
as
	declare @Orders_ID nchar(5),@Order_Details_Price money
	select @Orders_ID=Orders_ID,@Order_Details_Price=Order_Details_Price from inserted
	update Orders set Orders_Money=Orders_Money+@Order_Details_Price where Orders_ID=@Orders_ID 

--2.3进货信息修改（进货状态修改，到货时间修改，进货完成时间修改）
create proc Update_Orders @Orders_ID nchar(5),@Supplier_ID nchar(5),@Orders_ShippedDate date,@Orders_ArriveDate date,@Orders_Condition nvarchar(4),@Orders_FinishDate date
as
update Orders
set Supplier_ID=@Supplier_ID,Orders_ShippedDate=@Orders_ShippedDate,Orders_ArriveDate=@Orders_ArriveDate,Orders_Condition=@Orders_Condition,Orders_FinishDate=@Orders_FinishDate
where Orders_ID=@Orders_ID

/*create trigger tri_Orders_update on Orders for update(Orders_Condition)
as
	declare @Orders_ID nchar(5),@Orders_Condition_now nvarchar(4),@Orders_Condition_before nvarchar(4),@Orders_Money money
	set @Orders_ID=inserted.Orders_ID,@Orders_Condition_now=Orders_Condition from inserted 
	set @Orders_Condition_before=Orders_Condition from deleted
	select @Orders_Money=Orders_Money from Orders where Orders_ID=@Orders_ID
	if(@Orders_Condition_before='运输' and @Orders_Condition_now='入库')
		begin
		declare @Goods_ID nchar(4),@Order_Details_Quantity int
		declare Order_Details_cur scroll cursor 
		for
			select Goods_ID,Order_Details_Quantity
			from Order_Details
			where Orders_ID=@Orders_ID
		open Order_Details_cur
		fetch next from Order_Details_cur into @Goods_ID,@Order_Details_Quantity
		while @@FETCH_STATUS=0
			begin
			update Goods	
			set Goods_Qualitydate=DateAdd(mm,6,getDate()),Goods_Stock=Goods_Stock+@Order_Details_Quantity,Goods_Discount=1.0,Goods_Condition='在售',Goods_Outdatewarn='未过期'
			where Goods_ID=@Goods_ID
			declare @Goods_Stock int
			select @Goods_Stock=Goods_Stock from Goods where Goods_ID=@Goods_ID
			if(@Goods_Stock>=500)
				begin
				update Goods
				set Goods_Storewarn='库存正常'
				where Goods_ID=@Goods_ID
				end
			else
				begin
				update Goods
				set Goods_Storewarn='库存警告'
				where Goods_ID=@Goods_ID
				end
			fetch next from Order_Details_cur into @Goods_ID,@Order_Details_Quantity
			end
		close Order_Details_cur
		deallocate Order_Details_cur
		insert into Financial(Orders_ID,Financial_Type,Financial_Amount)
		values(@Orders_ID,'进货',-@Orders_Money)
		end
	else((@Orders_Condition_before='运输' and @Orders_Condition_now='退货'))
		begin
		insert into Financial(Orders_ID,Financial_Type,Financial_Amount)
		values(@Orders_ID,'退货',-@Orders_Money*0.1)
		end

1.在触发器中，应该使用 AFTER UPDATE 而不是 FOR UPDATE。
2.在设置变量的时候，应该使用 SELECT 语句而不是直接赋值。
3.在 IF...ELSE 语句中，应该使用 ELSE IF 而不是单独的 ELSE。
4.在 IF...ELSE 语句中，条件判断应该使用双等号（==）而不是单等号（=）。
5.在 END 后应该使用 END IF 而不是单独的 END。

*/

create trigger tri_Orders_update on Orders AFTER update
as
begin
	declare @Orders_ID nchar(5), @Orders_Condition_now nvarchar(4), @Orders_Condition_before nvarchar(4), @Orders_Money money
	select @Orders_ID = inserted.Orders_ID, @Orders_Condition_now = inserted.Orders_Condition from inserted 
	select @Orders_Condition_before = deleted.Orders_Condition from deleted
	select @Orders_Money = Orders_Money from Orders where Orders_ID = @Orders_ID

	if (@Orders_Condition_before = '运输' and @Orders_Condition_now = '入库')
	begin
		declare @Goods_ID nchar(4), @Order_Details_Quantity int
		declare Order_Details_cur cursor for
			select Goods_ID, Order_Details_Quantity
			from Order_Details
			where Orders_ID = @Orders_ID
		open Order_Details_cur
		fetch next from Order_Details_cur into @Goods_ID, @Order_Details_Quantity
		while @@FETCH_STATUS = 0
		begin
			update Goods	
			set Goods_Qualitydate = DateAdd(mm, 6, getDate()), Goods_Stock = Goods_Stock + @Order_Details_Quantity, Goods_Discount = 1.0, Goods_Condition = '在售', Goods_Outdatewarn = '未过期'
			where Goods_ID = @Goods_ID

			declare @Goods_Stock int
			select @Goods_Stock = Goods_Stock from Goods where Goods_ID = @Goods_ID
			if (@Goods_Stock >= 500)
			begin
				update Goods
				set Goods_Storewarn = '库存正常'
				where Goods_ID = @Goods_ID
			end
			else
			begin
				update Goods
				set Goods_Storewarn = '库存警告'
				where Goods_ID = @Goods_ID
			end

			fetch next from Order_Details_cur into @Goods_ID, @Order_Details_Quantity
		end
		close Order_Details_cur
		deallocate Order_Details_cur

		insert into Financial(Orders_ID, Financial_Type, Financial_Amount)
		values(@Orders_ID, '进货', -@Orders_Money)
	end
	else if (@Orders_Condition_before = '运输' and @Orders_Condition_now = '退货')
	begin
		insert into Financial(Orders_ID, Financial_Type, Financial_Amount)
		values(@Orders_ID, '退货', -@Orders_Money * 0.1)
	end
end



--3.库存管理
--3.1库存盘点
--查看单种商品库存量
create proc Query_Goods_Stock_Unit @Goods_ID nchar(4)
as
	select Goods_ID,Goods_UnitPrice,Goods_Stock
	from Goods
	where Goods_ID=@Goods_ID

--查看某类商品库存量
create proc Query_Goods_Stock_Kind @Goods_Type nvarchar(5)
as
	select Goods_ID,Goods_UnitPrice,Goods_Stock
	from Goods
	where Goods_Type=@Goods_Type

--3.2库存价格,状态调整（按月定时更新）
/*create proc Manage_Stock_Price 
as
	declare @Goods_ID nchar(4),@Goods_Qualitydate date
	declare Stock_cur cursor 
	for
		select Goods_ID,Goods_Qualitydate
		from Goods
	open Stock_cur
	fetch next from Stock_cur into @Goods_ID,@Goods_Qualitydate
	while @@FETCH_STATUS=0
	begin
		case (datediff(month,getdate(),@Goods_Qualitydate) )
		when 0 then update Goods set Goods_Stock=0,Goods_Condition='预售',Goods_Storewarn='库存警告' where Goods_ID=@Goods_ID
		when 1 then update Goods set Goods_Discount=0.6  where Goods_ID=@Goods_ID
		when 2 then update Goods set Goods_Discount=0.8  where Goods_ID=@Goods_ID
		end
		fetch next from Stock_cur into @Goods_ID,@Goods_Qualitydate
	end
*/
create proc Manage_Stock_Price 
as
begin
	SET NOCOUNT ON;

	declare @Goods_ID nchar(4), @Goods_Qualitydate date

		update Goods set Goods_Discount =
		case 
			when datediff(month, getdate(), @Goods_Qualitydate) = 1 then 0.6
			when datediff(month, getdate(), @Goods_Qualitydate) = 2 then 0.8
			else Goods_Discount end;
				-- Handle other cases if needed
		update Goods set Goods_Stock = 0, Goods_Condition = '预售', Goods_Storewarn = '库存警告' where datediff(month, getdate(), @Goods_Qualitydate) = 0 
End
--case when 的使用语法


--4.销售管理
--4.1销售单
create proc Manage_Sell @Sell_ID nchar(5),@Customer_ID nchar(5),@Employee_ID nchar(5),@Sell_OrderType nvarchar(6)
as
	insert into Sell(Sell_ID,Customer_ID,Employee_ID,Sell_OrderType)
	values(@Sell_ID,@Customer_ID,@Employee_ID,@Sell_OrderType)

--4.2销售细节管理
create proc Manage_SellInfo @Goods_ID nchar(4),@Sell_ID nchar(5),@SellInfo_Quantity int,@SellInfo_UnitPrice money,@SellInfo_Discount numeric(2,1),@SellInfo_Total money,@Goods_Type nvarchar(5)
as 
	insert into SellInfo(Goods_ID,Sell_ID,SellInfo_Quantity,SellInfo_UnitPrice,SellInfo_Discount,SellInfo_Total,Goods_Type)
	values(@Goods_ID,@Sell_ID,@SellInfo_Quantity,@SellInfo_UnitPrice,@SellInfo_Discount,@SellInfo_Total,@Goods_Type)

create trigger tri_Sell_TotalMoney on SellInfo for insert
as
	declare @Sell_ID nchar(5),@SellInfo_Total money
	select @Sell_ID=Sell_ID,@SellInfo_Total=SellInfo_Total from inserted
	update Sell set Sell_TotalMoney=Sell_TotalMoney+@SellInfo_Total where Sell_ID=@Sell_ID

--4.3销售库存管理
create trigger tr_Sell on SellInfo for insert
as
	declare @Goods_ID nchar(4),@Sell_ID nchar(5),@SellInfo_Total money,@SellInfo_Quantity int
	declare @Sell_OrderType nvarchar(6)
	select @Goods_ID=Goods_ID,@Sell_ID=Sell_ID,@SellInfo_Total=SellInfo_Total,@SellInfo_Quantity=SellInfo_Quantity from inserted 
	select @Sell_OrderType=Sell_OrderType from Sell where Sell_ID=@Sell_ID
	if @Sell_OrderType='现单'
	begin
		update Goods set Goods_Stock=Goods_Stock-@SellInfo_Quantity where Goods_ID=@Goods_ID
		insert into Financial(Sell_ID,Goods_ID,Financial_Type,Financial_Amount) values(@Sell_ID,@Goods_ID,'销售',@SellInfo_Total)
	end

create trigger tri_PreSell on Sell for update
as
begin
	if update(Sell_OrderType)
	begin
		declare @Sell_OrderType nvarchar(6), @Pre_Sell_OrderType nvarchar(6), @Sell_ID nchar(5)
		select @Pre_Sell_OrderType = Sell_OrderType from deleted 
		select @Sell_OrderType = Sell_OrderType, @Sell_ID = Sell_ID from inserted
		if @Pre_Sell_OrderType = '预订单' and @Sell_OrderType = '现单'
		begin 
			update Goods 
			set Goods_Stock = Goods_Stock - (select SellInfo_Quantity from SellInfo where Sell_ID = @Sell_ID)
			where Goods_ID in (select Goods_ID from SellInfo where Sell_ID = @Sell_ID)

			insert into Financial(Sell_ID, Goods_ID, Financial_Type, Financial_Amount) 
			select @Sell_ID, Goods_ID, '销售', SellInfo_Total
			from SellInfo where Sell_ID = @Sell_ID
		end
	end
End
--触发器内不能有游标

--4.4销售退货
/*create proc Return_Sell @Sell_ID nchar(5),@Employee_ID nchar(5),@Return_Date date,@Return_Charges money,@Return_Remark VARCHAR(MAX)
as 
	insert into Return_(Sell_ID,Employee_ID,Return_Date,Return_Charges,Return_Remark)
	values(@Sell_ID,@Employee_ID,@Return_Date,@Return_Charges,@Return_Remark)
	delete from Sell where Sell_ID=@Sell_ID cascade
*/
--delete 不支持级联删除，采用触发器
CREATE PROCEDURE Return_Sell 
    @Sell_ID nchar(5),
    @Employee_ID nchar(5),
    @Return_Date date,
    @Return_Charges money,
    @Return_Remark VARCHAR(MAX)
AS 
BEGIN
    INSERT INTO Return_(Sell_ID, Employee_ID, Return_Date, Return_Charges, Return_Remark)
    VALUES(@Sell_ID, @Employee_ID, @Return_Date, @Return_Charges, @Return_Remark)
	
    DELETE FROM Sell 
    WHERE Sell_ID = @Sell_ID
END

drop PROCEDURE Return_Sell 

CREATE TRIGGER tr_ReturnSell_Delete
ON Sell
AFTER DELETE
AS
BEGIN
    DELETE FROM SellInfo
    WHERE Sell_ID IN (SELECT Sell_ID FROM deleted)
END

drop PROCEDURE Return_Sell
drop TRIGGER tr_ReturnSell_Delete

CREATE PROCEDURE Return_Sell 
    @Sell_ID nchar(5),
    @Employee_ID nchar(5),
    @Return_Charges money,
    @Return_Remark VARCHAR(MAX)
AS 
BEGIN
    INSERT INTO Return_(Sell_ID, Employee_ID, Return_Charges, Return_Remark)
    VALUES(@Sell_ID, @Employee_ID, @Return_Charges, @Return_Remark)

	DELETE FROM SellInfo 
    WHERE Sell_ID = @Sell_ID

    DELETE FROM Sell 
    WHERE Sell_ID = @Sell_ID

	insert into Financial(Sell_ID, Financial_Type, Financial_Amount) values(@Sell_ID,'退款',@Return_Charges)
END

exec Return_Sell '004','A0001',10,'111'
select *from Financial
--5.查询统计
--5.1客户查询
--5.1.1给出客户ID可以查询客户信息
create proc Query_Customer_ID @Customer_ID nchar(5)
as
select *
from Customer
where Customer_ID=@Customer_ID

--5.1.2给出客户姓名可以查询客户信息,支持模糊查询
create proc Query_Customer_Name @Customer_Name nvarchar(20)
as
select *
from Customer
where Customer_Name like '%'+@Customer_Name+'%'

--5.1.3给出客户电话可以查询客户信息
create proc Query_Customer_Phone @Customer_Phone nchar(11)
as
select *
from Customer
where Customer_Phone=@Customer_Phone


--5.2商品查询
--5.2.1给出商品ID可以查看商品具体信息
create proc Query_Goods_ID @Goods_ID nchar(4)
as
select *
from Goods
where Goods_ID=@Goods_ID

--5.2.2给出商品类别名查询某类商品具体信息
create proc Query_Goods_Type @Goods_Type nvarchar(5)
as
select *
from Goods
where Goods_Type=@Goods_Type

--5.2.3根据给出的商品名查询商品信息，支持模糊查询
create proc Query_Goods_Name @Goods_Name nvarchar(30)
as
select *
from Goods
where Goods_Name like '%'+@Goods_Name+'%'

--5.3供应商查询
--5.3.1根据供应商号查询供应商信息
create proc Query_Supplier_ID @Supplier_ID nchar(5)
as
select *
from Suppliers
where Supplier_ID=@Supplier_ID

--5.3.2根据供应商名查询供应商信息,支持模糊查询
create proc Query_Supplier_Name @Supplier_Name nvarchar(20)
as
select *
from Suppliers
where Supplier_Name like '%'+@Supplier_Name+'%'

--5.3.3根据供应商地域查询供应商信息,支持模糊查询
create proc Query_Supplier_Address @Supplier_Address nvarchar(30)
as
select *
from Suppliers
where Supplier_Address like '%'+@Supplier_Address+'%'

--5.3.4根据供应商电话查询供应商信息
create proc Query_Supplier_Phone @Supplier_Phone nchar(11)
as
select *
from Suppliers
where Supplier_Phone=@Supplier_Phone


--5.4销售查询
--5.4.1根据销售单号查询销售信息
create proc Query_Sell_ID @Sell_ID nchar(5)
as
select *
from Sell
where Sell_ID=@Sell_ID

--5.4.2根据销售客户号查询销售信息
create proc Query_Sell_Customer_ID @Customer_ID nchar(5)
as
select *
from Sell
where Customer_ID=@Customer_ID

--5.4.3根据经手职工号查询销售信息
create proc Query_Sell_Employee_ID @Employee_ID nchar(5)
as
select *
from Sell
where Employee_ID=@Employee_ID

--5.4.4根据销售单种类查询销售信息 包括销售退货查询
create proc Query_Sell_OrderType @Sell_OrderType nvarchar(6)
as
select *
from Sell
where Sell_OrderType=@Sell_OrderType


--5.5进货查询
--5.5.1根据进货单号查询进货信息
create proc Query_Orders_ID @Orders_ID nchar(5)
as
select *
from Orders
where Orders_ID=@Orders_ID

--5.5.2根据供应商号查询进货信息
create proc Query_Orders_Supplier_ID @Supplier_ID nchar(5)
as
select *
from Orders
where Supplier_ID=@Supplier_ID

--5.5.3根据进货状态查询进货信息 包括进货退货查询
create proc Query_Orders_Condition @Orders_Condition nvarchar(4)
as
select *
from Orders
where Orders_Condition=@Orders_Condition

--5.6销售排行
--5.6.1按商品号排行 --降序
create proc Rank_Sell_ID
as
select G1.Goods_ID,Goods_Name,sum(SellInfo_Quantity) Quantity
from Goods G1,SellInfo S1
where S1.Goods_ID=G1.Goods_ID
group by G1.Goods_ID,Goods_Name
order by Quantity desc

--5.6.2按商品种类排序 --降序
create proc Rank_Sell_Goods_Type
as
select G1.Goods_Type,sum(SellInfo_Quantity)Quantity
from Goods G1,SellInfo S1
where G1.Goods_ID=S1.Goods_ID 
group by G1.Goods_Type
order by Quantity desc


--6.系统管理
--6.1操作员管理 添加员工类似处理，管理员为一种特殊员工
--6.1.1操作员添加
create proc Add_Employee @Employee_ID nchar(5),@Employee_Name nvarchar(20),@Employee_Salary money,@Job_ID nchar(1),@Employee_Phone nchar(11),@Employee_Epassword nvarchar(20),@Employee_Age int
as
insert
into Employee(Employee_ID,Employee_Name,Employee_Salary,Job_ID,Employee_Phone,Employee_Epassword,Employee_Age)
values(@Employee_ID,@Employee_Name,@Employee_Salary,@Job_ID,@Employee_Phone,@Employee_Epassword,@Employee_Age)

--6.1.2操作员删除
create proc Delete_Employee @Employee_ID nchar(5)
as
delete
from Employee
where Employee_ID=@Employee_ID

--6.2更改密码
create proc Update_Password @Employee_ID nchar(5),@Employee_Epassword nvarchar(20)
as
update Employee
set Employee_Epassword=@Employee_Epassword
where Employee_ID=@Employee_ID

--6.3权限管理
--6.3.1登陆系统
CREATE FUNCTION login_in(@Employee_ID nchar(5), @Employee_Epassword nvarchar(20))
RETURNS int
AS
BEGIN
    DECLARE @result int

    IF EXISTS (SELECT 1 FROM Employee WHERE Employee_ID = @Employee_ID AND Employee_Epassword = @Employee_Epassword)
    BEGIN
        SET @result = 1
    END
    ELSE
    BEGIN
        SET @result = 0
    END

    RETURN @result
END

--6.3.2修改权限
--6.3.2.1修改某位职工权限  管理员权限，通过前端显示界面实现只有管理员可以修改员工权限
create proc Update_Employee_Job @Employee_ID nchar(5),@Job_ID nchar(1)
as
update Employee
set Job_ID=@Job_ID
where Employee_ID=@Employee_ID

--6.3.2.2修改某权限具体职能
create proc Update_Job @Job_ID nchar(1),@Job_Info VARCHAR(MAX)
as
update Job
set Job_Info=@Job_Info
where Job_ID=@Job_ID


--6.4退出系统

--（二）额外功能实现
--1.系统可以对过期产品自动报警
go
create proc Warn_Outdate
as
declare @newdate date,@nowdate date
set @nowdate=GETDATE()
declare outdate_cur cursor for
select Goods_ID,Goods_Name,DATEDIFF(month,@nowdate,Goods_Qualitydate) InteralMonth
from Goods
open outdate_cur
declare @Goods_ID nchar(4),@Goods_Name nvarchar(30),@InteralMonth int
fetch next from outdate_cur into @Goods_ID,@Goods_Name,@InteralMonth
while @@FETCH_STATUS=0
begin
if(@InteralMonth<=1)
begin
update Goods
set Goods_Outdatewarn='过期警告'
where Goods_ID=@Goods_ID
end
fetch next from outdate_cur into @Goods_ID,@Goods_Name,@InteralMonth
end
close outdate_cur
deallocate outdate_cur

--2.对仓库中低于库存量的商品自动报警。--库存警告设为标准默认100,设置为固定值
create proc Warn_Stock
as
declare storewarn_cur cursor for
select Goods_ID,Goods_Stock
from Goods
open storewarn_cur
declare @Goods_ID nchar(4),@Goods_Stock int
fetch next from storewarn_cur into @Goods_ID,@Goods_Stock
while @@FETCH_STATUS=0
begin
if(@Goods_Stock<100)
begin
update Goods
set Goods_Storewarn='库存警告' 
where Goods_ID=@Goods_ID
end
fetch next from storewarn_cur into @Goods_ID,@Goods_Stock
end
close storewarn_cur
deallocate storewarn_cur

create proc DaySell
as
	SELECT Financial_Date, SUM(Financial_Amount) AS DailySales
	FROM Financial
	WHERE Financial_Date BETWEEN DATEADD(year, DATEDIFF(year, 0, GETDATE()), 0) AND GETDATE()
	GROUP BY Financial_Date
	ORDER BY Financial_Date desc;

exec DaySell

drop proc DaySell

create proc WeekSell
as
	SELECT DATEPART(ISO_WEEK, Financial_Date) AS WeekNumber, SUM(Financial_Amount) AS WeeklySales
	FROM Financial
	WHERE YEAR(Financial_Date) = YEAR(GETDATE())
	GROUP BY DATEPART(ISO_WEEK, Financial_Date)
	ORDER BY WeekNumber desc;

exec WeekSell

create proc MonthSell
as
	SELECT MONTH(Financial_Date) AS MonthNumber, SUM(Financial_Amount) AS MonthlySales
	FROM Financial
	WHERE YEAR(Financial_Date) = YEAR(GETDATE())
	GROUP BY MONTH(Financial_Date)
	ORDER BY MonthNumber desc;

exec MonthSell
