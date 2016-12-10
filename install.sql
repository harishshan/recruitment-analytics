--CREATE DATABASE RA;
--GO;
--USE RA;
CREATE TABLE [dbo].[Role](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[rolename] [nvarchar](50) NULL,
 CONSTRAINT [PK_Role] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];

INSERT INTO [dbo].[Role] ([rolename]) VALUES ('Admin');
INSERT INTO [dbo].[Role] ([rolename]) VALUES ('Recruiter');
INSERT INTO [dbo].[Role] ([rolename]) VALUES ('Employee');
INSERT INTO [dbo].[Role] ([rolename]) VALUES ('Accountant');
CREATE TABLE [dbo].[Login](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[username] [nvarchar](50) NULL,
	[password] [nvarchar](50) NULL,
	[emailId] [nvarchar](50) NULL,
	[roleId] [int] NULL,
 CONSTRAINT [PK_Login] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
),
CONSTRAINT [UK_Login] UNIQUE ([username])
) ON [PRIMARY];

ALTER TABLE [dbo].[Login] WITH CHECK ADD CONSTRAINT [FK_LOGIN_ROLE] FOREIGN KEY ([roleId]) REFERENCES [dbo].[Role] ([Id]);


INSERT INTO [dbo].[Login]([username], [password], [emailId], [roleId] ) SELECT 'harish', '123', 'harish_sci@hotmail.com', [id] FROM [dbo].[Role] WHERE [rolename] = 'Admin';
INSERT INTO [dbo].[Login]([username], [password], [emailId], [roleId] ) SELECT 'ashok', '123', 'ashokramcse@gmail.com', [id] FROM [dbo].[Role] WHERE [rolename] = 'Recruiter';
INSERT INTO [dbo].[Login]([username], [password], [emailId], [roleId] ) SELECT 'vijay', '123', 'vijay@gmail.com', [id] FROM [dbo].[Role] WHERE [rolename] = 'Employee';
INSERT INTO [dbo].[Login]([username], [password], [emailId], [roleId] ) SELECT 'murali', '123', 'murali@gmail.com', [id] FROM [dbo].[Role] WHERE [rolename] = 'Accountant';


CREATE TABLE [dbo].[Property](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](256) NULL,
	[value] [nvarchar](256) NULL,
 CONSTRAINT [PK_Property] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)ON [PRIMARY]);
INSERT INTO [dbo].[Property]([name], [value]) VALUES ('nativeStoreLocation','C:/data');
INSERT INTO [dbo].[Property]([name], [value]) VALUES ('nativeStoreLocation1','C:/data');