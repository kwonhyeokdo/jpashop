SELECT *
  FROM MEMBER m;
 
SELECT *
  FROM orders o;
 
SELECT *
  FROM delivery d;
 
SELECT *
  FROM order_item oi;
  
   SELECT DISTINCT
        o1_0.order_id,
        d1_0.delivery_id,
        d1_0.city,
        d1_0.street,
        d1_0.zipcode,
        d1_0.status,
        m1_0.member_id,
        m1_0.city,
        m1_0.street,
        m1_0.zipcode,
        m1_0.name,
        o1_0.order_date,
        oi1_0.order_id,
        oi1_0.order_item_id,
        oi1_0.count,
        i1_0.item_id,
        i1_0.dtype,
        i1_0.name,
        i1_0.price,
        i1_0.stock_quantity,
        i1_0.artist,
        i1_0.etc,
        i1_0.author,
        i1_0.isbn,
        i1_0.actor,
        i1_0.director,
        oi1_0.order_price,
        o1_0.status 
    from
        orders o1_0 
    join
        member m1_0 
            on m1_0.member_id=o1_0.member_id 
    join
        delivery d1_0 
            on d1_0.delivery_id=o1_0.delivery_id 
    join
        order_item oi1_0 
            on o1_0.order_id=oi1_0.order_id 
    join
        item i1_0 
            on i1_0.item_id=oi1_0.item_id