INSERT INTO hotel (nombre, ubicacion) VALUES
                                            ('Hotel Ibis', 'Trujillo'),
                                            ('Hotel Ibis', 'Lima');


INSERT INTO habitacion (categoria, img , precio, capacidad, descripcion, disponible,cantidad_total,cantidad_reservada, hotel_id) VALUES
                                                                       ('Estándar con 1 cama', 'https://cf.bstatic.com/xdata/images/hotel/max1024x768/380260919.jpg?k=870afc070b8657dcbb01066b633665fa3be38bb8a4ee6e289914447b30141c01&o=&hp=1', 120.0,2 , 'hola',true, 5,0,1), -- Habitación para Hotel con ID 1
                                                                       ('Estándar con 2 camas', 'https://www.ahstatic.com/photos/a8f4_rotwc_00_p_2048x1536.jpg', 170.0,2 , 'hola',true, 5,0,1),      -- Habitación para Hotel con ID 1
                                                                       ('Habitación deluxe', 'https://images.trvl-media.com/lodging/5000000/4300000/4290600/4290506/977fbf1f.jpg?impolicy=resizecrop&rw=1200&ra=fit', 2550.0,4 , 'hola', true,5, 0,1), -- Habitación para Hotel con ID 2
                                                                       ('Habitación deluxe con 2 cama doble', 'https://ibis.hotels-in-trujillo.com/data/Images/OriginalPhoto/11924/1192414/1192414624/image-trujillo-ibis-76.JPEG', 200.0,4 , 'hola', true, 5,0,1),     -- Habitación para Hotel con ID 2
                                                                       ('Estándar con cama doble', 'https://www.ahstatic.com/photos/a8f4_rodbc_00_p_2048x1536.jpg', 180.0,2 , 'hola', true,5, 0,1),   -- Habitación para Hotel con ID 3
                                                                       ('Habitación Premium con 1 cama king', 'https://www.ahstatic.com/photos/a8f4_rodbc_00_p_2048x1536.jpg', 300.0,2 , 'hola', true, 5,0,1);
