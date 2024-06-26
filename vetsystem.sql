PGDMP  5    1                |            vet_management    15.7    16.3 3    1
           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            2
           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            3
           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            4
           1262    16398    vet_management    DATABASE     �   CREATE DATABASE vet_management WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Turkish_T�rkiye.1254';
    DROP DATABASE vet_management;
                postgres    false            �            1259    16626    animal    TABLE     ?  CREATE TABLE public.animal (
    id bigint NOT NULL,
    animal_breed character varying(255),
    animal_color character varying(255),
    date_of_birth date,
    animal_gender character varying(255),
    animal_name character varying(255) NOT NULL,
    animal_species character varying(255),
    customer_id bigint
);
    DROP TABLE public.animal;
       public         heap    postgres    false            �            1259    16625 
   animal_id_seq    SEQUENCE     v   CREATE SEQUENCE public.animal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.animal_id_seq;
       public          postgres    false    215            5
           0    0 
   animal_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.animal_id_seq OWNED BY public.animal.id;
          public          postgres    false    214            �            1259    16635    appointment    TABLE     �   CREATE TABLE public.appointment (
    id bigint NOT NULL,
    appointment_date timestamp(6) without time zone,
    animal_id bigint,
    doctor_id bigint
);
    DROP TABLE public.appointment;
       public         heap    postgres    false            �            1259    16634    appointment_id_seq    SEQUENCE     {   CREATE SEQUENCE public.appointment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.appointment_id_seq;
       public          postgres    false    217            6
           0    0    appointment_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.appointment_id_seq OWNED BY public.appointment.id;
          public          postgres    false    216            �            1259    16642    avaliable_date    TABLE     n   CREATE TABLE public.avaliable_date (
    id bigint NOT NULL,
    avaliable_date date,
    doctor_id bigint
);
 "   DROP TABLE public.avaliable_date;
       public         heap    postgres    false            �            1259    16641    avaliable_date_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.avaliable_date_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.avaliable_date_id_seq;
       public          postgres    false    219            7
           0    0    avaliable_date_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.avaliable_date_id_seq OWNED BY public.avaliable_date.id;
          public          postgres    false    218            �            1259    16649    customer    TABLE       CREATE TABLE public.customer (
    id bigint NOT NULL,
    customer_address character varying(255),
    customer_city character varying(255),
    customer_mail character varying(255),
    customer_name character varying(255),
    customer_phone character varying(255)
);
    DROP TABLE public.customer;
       public         heap    postgres    false            �            1259    16648    customer_id_seq    SEQUENCE     x   CREATE SEQUENCE public.customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.customer_id_seq;
       public          postgres    false    221            8
           0    0    customer_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.customer_id_seq OWNED BY public.customer.id;
          public          postgres    false    220            �            1259    16658    doctor    TABLE       CREATE TABLE public.doctor (
    id bigint NOT NULL,
    doctor_address character varying(255),
    doctor_city character varying(255),
    doctor_mail character varying(255),
    doctor_name character varying(255),
    doctor_phone character varying(255)
);
    DROP TABLE public.doctor;
       public         heap    postgres    false            �            1259    16657 
   doctor_id_seq    SEQUENCE     v   CREATE SEQUENCE public.doctor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.doctor_id_seq;
       public          postgres    false    223            9
           0    0 
   doctor_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.doctor_id_seq OWNED BY public.doctor.id;
          public          postgres    false    222            �            1259    16667    vaccine    TABLE     �   CREATE TABLE public.vaccine (
    id bigint NOT NULL,
    vaccine_code character varying(255),
    vaccine_name character varying(255),
    protection_finish_date date,
    protection_start_date date,
    animal_id bigint
);
    DROP TABLE public.vaccine;
       public         heap    postgres    false            �            1259    16666    vaccine_id_seq    SEQUENCE     w   CREATE SEQUENCE public.vaccine_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.vaccine_id_seq;
       public          postgres    false    225            :
           0    0    vaccine_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.vaccine_id_seq OWNED BY public.vaccine.id;
          public          postgres    false    224            ~           2604    16629 	   animal id    DEFAULT     f   ALTER TABLE ONLY public.animal ALTER COLUMN id SET DEFAULT nextval('public.animal_id_seq'::regclass);
 8   ALTER TABLE public.animal ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    214    215    215                       2604    16638    appointment id    DEFAULT     p   ALTER TABLE ONLY public.appointment ALTER COLUMN id SET DEFAULT nextval('public.appointment_id_seq'::regclass);
 =   ALTER TABLE public.appointment ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    216    217            �           2604    16645    avaliable_date id    DEFAULT     v   ALTER TABLE ONLY public.avaliable_date ALTER COLUMN id SET DEFAULT nextval('public.avaliable_date_id_seq'::regclass);
 @   ALTER TABLE public.avaliable_date ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    219    218    219            �           2604    16652    customer id    DEFAULT     j   ALTER TABLE ONLY public.customer ALTER COLUMN id SET DEFAULT nextval('public.customer_id_seq'::regclass);
 :   ALTER TABLE public.customer ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    220    221    221            �           2604    16661 	   doctor id    DEFAULT     f   ALTER TABLE ONLY public.doctor ALTER COLUMN id SET DEFAULT nextval('public.doctor_id_seq'::regclass);
 8   ALTER TABLE public.doctor ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    223    222    223            �           2604    16670 
   vaccine id    DEFAULT     h   ALTER TABLE ONLY public.vaccine ALTER COLUMN id SET DEFAULT nextval('public.vaccine_id_seq'::regclass);
 9   ALTER TABLE public.vaccine ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    224    225    225            $
          0    16626    animal 
   TABLE DATA           �   COPY public.animal (id, animal_breed, animal_color, date_of_birth, animal_gender, animal_name, animal_species, customer_id) FROM stdin;
    public          postgres    false    215   A:       &
          0    16635    appointment 
   TABLE DATA           Q   COPY public.appointment (id, appointment_date, animal_id, doctor_id) FROM stdin;
    public          postgres    false    217   6;       (
          0    16642    avaliable_date 
   TABLE DATA           G   COPY public.avaliable_date (id, avaliable_date, doctor_id) FROM stdin;
    public          postgres    false    219   �;       *
          0    16649    customer 
   TABLE DATA           u   COPY public.customer (id, customer_address, customer_city, customer_mail, customer_name, customer_phone) FROM stdin;
    public          postgres    false    221   <       ,
          0    16658    doctor 
   TABLE DATA           i   COPY public.doctor (id, doctor_address, doctor_city, doctor_mail, doctor_name, doctor_phone) FROM stdin;
    public          postgres    false    223   �<       .
          0    16667    vaccine 
   TABLE DATA           {   COPY public.vaccine (id, vaccine_code, vaccine_name, protection_finish_date, protection_start_date, animal_id) FROM stdin;
    public          postgres    false    225   �=       ;
           0    0 
   animal_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.animal_id_seq', 7, true);
          public          postgres    false    214            <
           0    0    appointment_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.appointment_id_seq', 10, true);
          public          postgres    false    216            =
           0    0    avaliable_date_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.avaliable_date_id_seq', 10, true);
          public          postgres    false    218            >
           0    0    customer_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.customer_id_seq', 6, true);
          public          postgres    false    220            ?
           0    0 
   doctor_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.doctor_id_seq', 6, true);
          public          postgres    false    222            @
           0    0    vaccine_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.vaccine_id_seq', 7, true);
          public          postgres    false    224            �           2606    16633    animal animal_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.animal
    ADD CONSTRAINT animal_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.animal DROP CONSTRAINT animal_pkey;
       public            postgres    false    215            �           2606    16640    appointment appointment_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.appointment DROP CONSTRAINT appointment_pkey;
       public            postgres    false    217            �           2606    16647 "   avaliable_date avaliable_date_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.avaliable_date
    ADD CONSTRAINT avaliable_date_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.avaliable_date DROP CONSTRAINT avaliable_date_pkey;
       public            postgres    false    219            �           2606    16656    customer customer_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_pkey;
       public            postgres    false    221            �           2606    16665    doctor doctor_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.doctor
    ADD CONSTRAINT doctor_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.doctor DROP CONSTRAINT doctor_pkey;
       public            postgres    false    223            �           2606    16674    vaccine vaccine_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.vaccine
    ADD CONSTRAINT vaccine_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.vaccine DROP CONSTRAINT vaccine_pkey;
       public            postgres    false    225            �           2606    16680 '   appointment fk2kkeptdxfuextg5ch7xp3ytie 
   FK CONSTRAINT     �   ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk2kkeptdxfuextg5ch7xp3ytie FOREIGN KEY (animal_id) REFERENCES public.animal(id);
 Q   ALTER TABLE ONLY public.appointment DROP CONSTRAINT fk2kkeptdxfuextg5ch7xp3ytie;
       public          postgres    false    3205    217    215            �           2606    16675 "   animal fk6pvxm5gfjqxclb651be9unswe 
   FK CONSTRAINT     �   ALTER TABLE ONLY public.animal
    ADD CONSTRAINT fk6pvxm5gfjqxclb651be9unswe FOREIGN KEY (customer_id) REFERENCES public.customer(id);
 L   ALTER TABLE ONLY public.animal DROP CONSTRAINT fk6pvxm5gfjqxclb651be9unswe;
       public          postgres    false    3211    215    221            �           2606    16690 *   avaliable_date fkmakb410pg66lj67fl2qwas6d1 
   FK CONSTRAINT     �   ALTER TABLE ONLY public.avaliable_date
    ADD CONSTRAINT fkmakb410pg66lj67fl2qwas6d1 FOREIGN KEY (doctor_id) REFERENCES public.doctor(id);
 T   ALTER TABLE ONLY public.avaliable_date DROP CONSTRAINT fkmakb410pg66lj67fl2qwas6d1;
       public          postgres    false    223    3213    219            �           2606    16695 #   vaccine fkne3kmh8y5pcyxwl4u2w9prw6j 
   FK CONSTRAINT     �   ALTER TABLE ONLY public.vaccine
    ADD CONSTRAINT fkne3kmh8y5pcyxwl4u2w9prw6j FOREIGN KEY (animal_id) REFERENCES public.animal(id);
 M   ALTER TABLE ONLY public.vaccine DROP CONSTRAINT fkne3kmh8y5pcyxwl4u2w9prw6j;
       public          postgres    false    225    215    3205            �           2606    16685 '   appointment fkoeb98n82eph1dx43v3y2bcmsl 
   FK CONSTRAINT     �   ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fkoeb98n82eph1dx43v3y2bcmsl FOREIGN KEY (doctor_id) REFERENCES public.doctor(id);
 Q   ALTER TABLE ONLY public.appointment DROP CONSTRAINT fkoeb98n82eph1dx43v3y2bcmsl;
       public          postgres    false    3213    217    223            $
   �   x�e�Aj�0E�_��Zl'N�b�R�:
�(����VK߾���Ka�b�>�M��7�ӏ���4����./dp0��J�3>��Rm��s���m	�X����^	��o�o�ه-���'�|���m��BU�λ9�m�3��OFǮ3�a��m�n	6��
팼�U�s�a�eC*��}�i2�r�O)Q��;�a�R���*��L{1v1�^�뎣�7A.��WJ�|_a�      &
   d   x�]��� ��&
�%�$��e�c���~6�ʨ��^�#mxn����%bY�y��V���R�Y�b6�K`_6��s�k[�Ϟ`zu���ݥ����*�      (
   K   x�Mʻ
�0���?c{��?G(,��S2�`lN
kF��4D3�1�R�K
��dXM-��ɲ(Q�z�����A      *
   �   x�U�Mn�0���� 9�O�k6�TuU�l��X�9�� ��
��U'j�t��I3of8V�[�Ѣ���������5����rdp���ʰ����L5���q��l�4Z�,X�%EW%ei�;5�6|�I�2 ta�<˙��B��΄2�@�Py玁�Sh!�X�]��!�r�?�zd��)b�+l�n���jO�N���O���l�`�� z�b�      ,
   �   x�]�;n�@E�7�A��݅H�°����|,9��B���Jgg_<'��R�^�{���k�|�	�KB�:�懡�B*]Z�8����=_��l1�O������6!P\+)�e��H�������Ɩ�&��v���Nj!��qNW�QB�Bڒ)���������.���	�H.HA
����K<=}����;Ù�"�)�`�c���er      .
   �   x���11F�|��ҦMow?9J���8x'��Q!�#�<�(�)�s�3�e1֭ӑ����4il�z���V��G�P�o%@hJ4�[��R5$�Y6D��@�.�z}$�[mm1|[k+��^q�i�	��6E     